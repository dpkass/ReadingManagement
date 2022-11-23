package Processing.Displayer;

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

    static void list(Stream<String> parts, Stream<Entry> entrystream) {
        // remove command and duplicates and distinguish filters/sort/categorize from elements to list
        Map<Boolean, List<String>> type0filterMap = parts.map(Helper::representation)
                                                         .distinct()
                                                         .collect(Collectors.partitioningBy(s -> s.matches(".*[=<>].*")));
        // get printing function
        listfunctions(type0filterMap.get(false));
        // split sorting and categorizing commands in a map
        Map<String, String[]> orderMap = getOrderStrings(type0filterMap.get(true));

        Comparator<Entry> sorter = getSorter(orderMap.get("sb"));
        entrystream = filterStream(type0filterMap.get(true), entrystream);

        putResult(entrystream, orderMap, sorter);
        makeHeaderList(type0filterMap.get(false));
    }

    private static void makeHeaderList(List<String> strings) {
        // later: get header name through helper
        strings.add(0, "Name");
        rr.setHeaderlist(strings);
    }

    @NotNull
    private static void putResult(Stream<Entry> entrystream, Map<String, String[]> orderMap, Comparator<Entry> sorter) {
        // if not grouped print normally
        String[] obs = orderMap.get("gb");
        if (obs == null) {
            rr.setList(entrystream.sorted(sorter).toList());
            rr.setType(RequestResult.RequestResultType.LIST);
        } else {
            SortedMap<?, List<Entry>> groupedLists = groupToMap(entrystream, obs[1]);
            putGroupedResult(groupedLists, sorter);
            rr.setType(RequestResult.RequestResultType.GROUPEDLIST);
        }
    }

    private static void putGroupedResult(SortedMap<?, List<Entry>> groupedLists, Comparator<Entry> sorter) {
        groupedLists.values().forEach(a -> a.sort(sorter));
        rr.setGroupedmap(groupedLists);
    }

    private static SortedMap<?, List<Entry>> groupToMap(Stream<Entry> entrystream, String groupby) {
        return switch (Helper.representation(groupby)) {
            case "ws" -> makeWSMap(entrystream);
            case "rs" -> makeRSMap(entrystream);
            case "r", "rtg" -> makeIntMap(entrystream, groupby);
            case "lr" -> makeLDMap(entrystream);
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

    private static SortedMap<Integer, List<Entry>> makeIntMap(Stream<Entry> entrystream, String groupby) {
        Function<Entry, Integer> f = null;
        switch (Helper.representation(groupby)) {
            case "r" -> f = e -> (int) (e.readto()) / 50 * 50;
            case "rtg" -> f = e -> (int) (e.rating());
        }
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

    private static Comparator<Entry> getSorter(String[] sortArgs) {
        if (sortArgs == null) return Comparator.comparing(Entry::readto);
        else {
            Comparator<Entry> comp = switch (Helper.representation(sortArgs[1])) {
                case "r" -> Comparator.comparing(Entry::readto);
                case "rtg" -> Comparator.comparing(Entry::rating);
                case "n" -> Comparator.comparing(Entry::name);
                case "lr" -> Comparator.comparing(Entry::lastread);
                case "pu" -> Comparator.comparing(Entry::pauseduntil);
                case "ws" -> Comparator.comparing(Entry::writingStatus);
                case "rs" -> Comparator.comparing(Entry::readingStatus);
                default -> throw new IllegalArgumentException("1");
            };

            if (sortArgs.length > 2 && Helper.representation(sortArgs[2]).equals("desc")) return comp.reversed();
            return comp;
        }
    }

    private static Stream<Entry> filterStream(List<String> filterstrings, Stream<Entry> stream) {
        List<String[]> filters = filterstrings.stream().map(filter -> filter.split("((?<=[=<>])|(?=[=<>]))")).toList();
        for (String[] filter : filters) {
            Predicate<Entry> p = getFilter(filter);
            stream = stream.filter(p);
        }
        return stream;
    }

    @NotNull
    private static Predicate<Entry> getFilter(String[] filter) {
        String[] orFilter = filter[2].split("OR");
        if (orFilter.length > 1) return complexFilter(filter, orFilter);
        return simpleFilter(filter);
    }

    private static Predicate<Entry> complexFilter(String[] filter, String[] orFilter) {
        if (!filter[1].equals("=")) throw new IllegalArgumentException("1");
        return e -> {
            for (String f : orFilter)
                if (getEqFilter(filter[0], f).test(e)) return true;
            return false;
        };
    }

    @NotNull
    private static Predicate<Entry> simpleFilter(String[] filter) {
        return switch (filter[1]) {
            case "=" -> getEqFilter(filter[0], filter[2]);
            case "<", ">" -> getUneqFilter(filter);
            default -> throw new IllegalStateException();
        };
    }

    private static Predicate<Entry> getEqFilter(String filterBy, String f) {
        return e -> switch (Helper.representation(filterBy)) {
            case "rtg" -> e.rating() == Float.parseFloat(f);
            case "ws" -> Objects.equals(Objects.toString(e.writingStatus()), f);
            case "rs" -> Objects.equals(Objects.toString(e.readingStatus()), f);
            default -> throw new IllegalArgumentException("1");
        };
    }

    @NotNull
    private static Predicate<Entry> getUneqFilter(String[] filter) {
        return e -> switch (Helper.representation(filter[0])) {
            case "lr" -> getDateFilter(filter, e.lastread());
            case "pu" -> getDateFilter(filter, e.pauseduntil());
            case "r" -> getNumberFilter(filter, e.readto());
            case "rtg" -> getNumberFilter(filter, e.rating());
            default -> throw new IllegalArgumentException("1");
        };
    }

    private static boolean getDateFilter(String[] filter, Temporal filterTemporal) {
        if (filterTemporal == null) return true;
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate inputTime = LocalDate.parse(filter[2], dtf);
            LocalDate filterdate = LocalDate.from(filterTemporal);

            if (Objects.equals(filter[1], "<")) return filterdate.isBefore(inputTime);
            else if (Objects.equals(filter[1], ">")) return filterdate.isAfter(inputTime);
            else throw new IllegalStateException();
        } catch (DateTimeParseException nfe) {
            throw new IllegalArgumentException("1");
        }
    }

    private static boolean getNumberFilter(String[] filter, float num) {
        try {
            if (Objects.equals(filter[1], "<")) return num < Float.parseFloat(filter[2]);
            else return num > Float.parseFloat(filter[2]);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("1");
        }
    }

    private static void listfunctions(List<String> typeList) {
        TableDataSupplier tds = new TableDataSupplier();
        typeList.stream().map(DisplayUtil::getFunction).forEach(tds::add);
        rr.setDatasupplier(tds);
    }
}
