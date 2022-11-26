package Processing.Displayer;

import AppRunner.Datastructures.Attribute;
import AppRunner.Datastructures.DisplayAttributesForm;
import AppRunner.Datastructures.DisplayAttributesUtil;
import AppRunner.Datastructures.Filter;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import EntryHandling.Entry.ReadingStatus;
import EntryHandling.Entry.WritingStatus;
import Management.Helper;
import Processing.RequestResult;
import Processing.TableDataSupplier;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Processing.Processor.rr;
import static java.util.stream.Collectors.toList;

class Lister {

    static void list(Stream<Entry> entries, DisplayAttributesForm daf, List<Filter<?>> filters, Attribute sortby, Attribute groupby) {
        listfunctions(daf);
        Comparator<Entry> sorter = getSorter(sortby);
        entries = filterStream(entries, filters);
        putResult(entries, sorter, groupby);
        makeHeaderList(daf);
    }

    private static void makeHeaderList(DisplayAttributesForm daf) {
        List<String> rrtableheader = new ArrayList<>();
        rrtableheader.add("Name");
        DisplayAttributesUtil.stream(daf).map(Attribute::displayvalue).forEach(rrtableheader::add);
        rr.setHeaderlist(rrtableheader);
    }

    @NotNull
    private static void putResult(Stream<Entry> entrystream, Comparator<Entry> sorter, Attribute groupby) {
        if (groupby == null) {
            rr.setList(entrystream.sorted(sorter).toList());
            rr.setType(RequestResult.RequestResultType.LIST);
        } else {
            SortedMap<?, List<Entry>> groupedLists = groupToMap(entrystream, groupby);
            putGroupedResult(groupedLists, sorter);
            rr.setType(RequestResult.RequestResultType.GROUPEDLIST);
        }
    }

    private static void putGroupedResult(SortedMap<?, List<Entry>> groupedLists, Comparator<Entry> sorter) {
        groupedLists.values().forEach(a -> a.sort(sorter));
        rr.setGroupedmap(groupedLists);
    }

    private static SortedMap<?, List<Entry>> groupToMap(Stream<Entry> entrystream, Attribute groupby) {
        return switch (groupby) {
            case writingStatus -> makeWSMap(entrystream);
            case readingStatus -> makeRSMap(entrystream);
            case readto -> makeIntMap(entrystream, e -> (int) (e.readto()) / 50 * 50);
            case rating -> makeIntMap(entrystream, e -> (int) (e.rating()));
            case lastread -> makeLDMap(entrystream);
            default -> throw new IllegalArgumentException("1");
        };
    }

    private static SortedMap<WritingStatus, List<Entry>> makeWSMap(Stream<Entry> entrystream) {
        Function<Entry, WritingStatus> f = Entry::writingStatus;
        return entrystream.collect(Collectors.groupingBy(f, () -> new TreeMap<>(Comparator.comparing(WritingStatus::ordinal)), toList()));
    }

    private static SortedMap<ReadingStatus, List<Entry>> makeRSMap(Stream<Entry> entrystream) {
        Function<Entry, ReadingStatus> f = Entry::readingStatus;
        return entrystream.collect(Collectors.groupingBy(f, () -> new TreeMap<>(Comparator.comparing(ReadingStatus::ordinal)), toList()));
    }

    private static SortedMap<Integer, List<Entry>> makeIntMap(Stream<Entry> entrystream, Function<Entry, Integer> f) {
        return entrystream.collect(Collectors.groupingBy(f, TreeMap::new, toList()));
    }

    private static SortedMap<String, List<Entry>> makeLDMap(Stream<Entry> entrystream) {
        Function<Entry, String> f = e -> EntryUtil.dateString(e.lastread(), DateTimeFormatter.ofPattern("MM-yyyy"), "-");
        return entrystream.collect(Collectors.groupingBy(f, TreeMap::new, toList()));
    }

    private static Map<String, String[]> getOrderStrings(List<String> filterList) {
        String[] orderStrings = filterList.stream().filter(isOrderString()).toArray(String[]::new);

        Map<String, String[]> result = new HashMap<>();
        for (String s : orderStrings) {
            String[] parts = s.split("=");
            boolean duplicate = false;
            switch (Helper.representation(parts[0])) {
                case "sb" -> duplicate = result.putIfAbsent("sb", parts) != null;
                case "gb" -> duplicate = result.putIfAbsent("gb", parts) != null;
            }
            if (duplicate) throw new IllegalArgumentException("1");
            filterList.remove(s);
        }
        return result;
    }

    @NotNull
    private static Predicate<String> isOrderString() {
        return s -> s.startsWith("sb") || s.startsWith("sortBy") || s.startsWith("sortby") || s.startsWith("sort") || s.startsWith("gb") || s.startsWith("groupBy") || s.startsWith("groupby") || s.startsWith("group");
    }

    private static Comparator<Entry> getSorter(Attribute sortby) {
        if (sortby == null) return Comparator.comparing(Entry::name);
        else {
//            rework sort descending
//            if (sortArgs.length > 2 && Helper.representation(sortArgs[2]).equals("desc")) return comp.reversed();

            return switch (sortby) {
                case name -> Comparator.comparing(Entry::name);
                case readto -> Comparator.comparing(Entry::readto);
                case rating -> Comparator.comparing(Entry::rating);
                case lastread -> Comparator.comparing(Entry::lastread, Comparator.nullsFirst(Comparator.naturalOrder()));
                case pauseduntil -> Comparator.comparing(Entry::pauseduntil, Comparator.nullsFirst(Comparator.naturalOrder()));
                case writingStatus -> Comparator.comparing(Entry::writingStatus);
                case readingStatus -> Comparator.comparing(Entry::readingStatus);
                default -> throw new IllegalArgumentException("1");
            };
        }
    }

    private static Stream<Entry> filterStream(Stream<Entry> stream, List<Filter<?>> filters) {
        if (filters == null) return stream;
        for (Filter<?> filter : filters) {
            Predicate<Entry> p = getFilter(filter);
            stream = stream.filter(p);
        }
        return stream;
    }

    @NotNull
    private static Predicate<Entry> getFilter(Filter<?> filter) {
        if (filter.isOrFilter()) return complexFilter(filter);
        return simpleFilter(filter);
    }

    private static Predicate<Entry> complexFilter(Filter<?> filter) {
        if (!filter.operator().equals("=")) throw new IllegalArgumentException("1");
        return e -> {
            for (String f : filter.splitValue())
                if (getStringEqFilter(filter.attribute(), f).test(e)) return true;
            return false;
        };
    }

    @NotNull
    private static Predicate<Entry> simpleFilter(Filter<?> filter) {
        return switch (filter.operator()) {
            case "=" -> getEqFilter(filter.attribute(), filter.value());
            case "<", ">" -> getUneqFilter(filter);
            default -> throw new IllegalStateException();
        };
    }

    private static Predicate<Entry> getEqFilter(Attribute attribute, Object value) {
        if (value instanceof String) return getStringEqFilter(attribute, (String) value);
        if (value instanceof Float) return getFloatEqFilter(attribute, ((Float) value).floatValue());
        throw new IllegalStateException();
    }

    private static Predicate<Entry> getFloatEqFilter(Attribute attribute, float value) {
        return e -> switch (attribute) {
            case rating -> e.rating() == value;
            default -> throw new IllegalArgumentException("1");
        };
    }

    private static Predicate<Entry> getStringEqFilter(Attribute filterBy, String s) {
        return e -> switch (filterBy) {
            case writingStatus -> Objects.equals(e.writingStatus(), WritingStatus.getStatus(s));
            case readingStatus -> Objects.equals(e.readingStatus(), ReadingStatus.getStatus(s));
            default -> throw new IllegalArgumentException("1");
        };
    }

    @NotNull
    private static Predicate<Entry> getUneqFilter(Filter<?> filter) {
        return e -> switch (filter.attribute()) {
            case readto -> getFloatUneqFilter(filter.operator(), (Float) filter.value(), e.readto());
            case rating -> getFloatUneqFilter(filter.operator(), (Float) filter.value(), e.rating());
            case lastread -> getDateUneqFilter(filter.operator(), (LocalDate) filter.value(), e.lastread());
            case pauseduntil -> getDateUneqFilter(filter.operator(), (LocalDate) filter.value(), e.pauseduntil());
            default -> throw new IllegalArgumentException("1");
        };
    }

    private static boolean getDateUneqFilter(String operator, LocalDate filterdate, Temporal entrydate) {
        if (entrydate == null) return true;
        try {
            LocalDate entryld = LocalDate.from(entrydate);

            if (Objects.equals(operator, "<")) return entryld.isBefore(filterdate);
            else if (Objects.equals(operator, ">")) return entryld.isAfter(filterdate);
            else throw new IllegalStateException();
        } catch (DateTimeParseException nfe) {
            throw new IllegalArgumentException("1");
        }
    }

    private static boolean getFloatUneqFilter(String operator, float filtervalue, float entryvalue) {
        try {
            if (Objects.equals(operator, "<")) return entryvalue < filtervalue;
            else return entryvalue > filtervalue;
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("1");
        }
    }

    private static void listfunctions(DisplayAttributesForm daf) {
        TableDataSupplier tds = new TableDataSupplier();
        DisplayAttributesUtil.stream(daf).map(DisplayUtil::getFunction).forEach(tds::add);
        rr.setDatasupplier(tds);
    }
}
