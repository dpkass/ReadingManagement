package Processing;

import AppRunner.Data.Containers.Request;
import AppRunner.Data.Containers.RequestResult;
import AppRunner.Data.Types.Attribute;
import AppRunner.Data.Containers.DisplayAttributesForm;
import AppRunner.Data.Containers.Filter;
import AppRunner.Data.Types.Operator;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import EntryHandling.Entry.ReadingStatus;
import EntryHandling.Entry.WritingStatus;
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

public class Displayer {
    public static void list(Stream<Entry> entries, DisplayAttributesForm daf, Set<Filter<?>> filters, Attribute sortby, Attribute groupby, boolean sortdescending, boolean groupdescending) {
        Lister.list(entries, daf, filters, sortby, groupby, sortdescending, groupdescending);
    }

    public static void recommend() {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        String twoWeeksAgoString = twoWeeksAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Request rq = new Request();
        rq.setOperator(Operator.List);
        rq.setGroupby("rs");
        Filter<?> filter = Filter.createFilter(Attribute.ReadingStatus, "=", Set.of("Reading", "Started", "Not-Started"));
        Filter<?> filter2 = Filter.createFilter(Attribute.WritingStatus, "=", Set.of("Rolling"));
        Filter<?> filter3 = Filter.createFilter(Attribute.LastRead, "<", twoWeeksAgoString);
        rq.setFilters(Set.of(filter, filter2, filter3));

        Processor.doList(rq);
    }

    static class Lister {

        static void list(Stream<Entry> entries, DisplayAttributesForm daf, Set<Filter<?>> filters, Attribute sortby, Attribute groupby, boolean sortdescending, boolean groupdescending) {
            listfunctions(daf);
            Comparator<Entry> sorter = getSorter(sortby, sortdescending);
            entries = filterStream(entries, filters);
            putResult(entries, sorter, groupby, groupdescending);
            makeHeaderList(daf);
        }

        private static void makeHeaderList(DisplayAttributesForm daf) {
            List<String> tableheader = new ArrayList<>();
            tableheader.add("Name");
            daf.stream().map(Attribute::displayvalue).forEach(tableheader::add);
            rr.setHeaderlist(tableheader);
        }

        private static void putResult(Stream<Entry> entrystream, Comparator<Entry> sorter, Attribute groupby, boolean groupdescending) {
            if (groupby == null) {
                rr.setList(entrystream.sorted(sorter).toList());
                rr.setType(RequestResult.RequestResultType.LIST);
            } else {
                SortedMap<?, List<Entry>> groupedMap = groupToMap(entrystream, groupby, groupdescending);
                putGroupedResult(groupedMap, sorter);
                rr.setType(RequestResult.RequestResultType.GROUPEDLIST);
            }
        }

        private static void putGroupedResult(SortedMap<?, List<Entry>> groupedMap, Comparator<Entry> sorter) {
            groupedMap.values().forEach(a -> a.sort(sorter));
            rr.setGroupedmap(groupedMap);
        }

        private static SortedMap<?, List<Entry>> groupToMap(Stream<Entry> entrystream, Attribute groupby, boolean groupdescending) {
            return switch (groupby) {
                case WritingStatus -> makeWSMap(entrystream, groupdescending);
                case ReadingStatus -> makeRSMap(entrystream, groupdescending);
                case ReadTo -> makeIntMap(entrystream, e -> (int) (e.readto()) / 50 * 50, groupdescending);
                case Rating -> makeIntMap(entrystream, e -> (int) (e.rating()), groupdescending);
                case LastRead -> makeLDMap(entrystream, groupdescending);
                default -> throw new IllegalArgumentException("1");
            };
        }

        private static SortedMap<WritingStatus, List<Entry>> makeWSMap(Stream<Entry> entrystream, boolean groupdescending) {
            Function<Entry, WritingStatus> f = Entry::writingStatus;
            final Comparator<WritingStatus> comp = groupdescending ? Comparator.reverseOrder() : Comparator.naturalOrder();
            return entrystream.collect(Collectors.groupingBy(f, () -> new TreeMap<>(comp), toList()));
        }

        private static SortedMap<ReadingStatus, List<Entry>> makeRSMap(Stream<Entry> entrystream, boolean groupdescending) {
            Function<Entry, ReadingStatus> f = Entry::readingStatus;
            final Comparator<ReadingStatus> comp = groupdescending ? Comparator.reverseOrder() : Comparator.naturalOrder();
            return entrystream.collect(Collectors.groupingBy(f, () -> new TreeMap<>(comp), toList()));
        }

        private static SortedMap<Integer, List<Entry>> makeIntMap(Stream<Entry> entrystream, Function<Entry, Integer> f, boolean groupdescending) {
            final Comparator<Integer> comp = groupdescending ? Comparator.reverseOrder() : Comparator.naturalOrder();
            return entrystream.collect(Collectors.groupingBy(f, () -> new TreeMap<>(comp), toList()));
        }

        private static SortedMap<String, List<Entry>> makeLDMap(Stream<Entry> entrystream, boolean groupdescending) {
            Function<Entry, String> f = e -> EntryUtil.dateString(e.lastread(), DateTimeFormatter.ofPattern("MM-yyyy"), "-");
            final Comparator<String> comp = groupdescending ? Comparator.reverseOrder() : Comparator.naturalOrder();
            return entrystream.collect(Collectors.groupingBy(f, () -> new TreeMap<>(comp), toList()));
        }

        private static Comparator<Entry> getSorter(Attribute sortby, boolean sortdescending) {
            if (sortby == null) return Comparator.comparing(Entry::name);
            else {
                Comparator<Entry> comp = switch (sortby) {
                    case Name -> Comparator.comparing(Entry::name);
                    case ReadTo -> Comparator.comparing(Entry::readto);
                    case Rating -> Comparator.comparing(Entry::rating);
                    case LastRead -> Comparator.comparing(Entry::lastread, Comparator.nullsFirst(Comparator.naturalOrder()));
                    case WaitUntil -> Comparator.comparing(Entry::waituntil, Comparator.nullsFirst(Comparator.naturalOrder()));
                    case WritingStatus -> Comparator.comparing(Entry::writingStatus);
                    case ReadingStatus -> Comparator.comparing(Entry::readingStatus);
                    default -> throw new IllegalArgumentException("1");
                };

                if (sortdescending) return comp.reversed();
                return comp;
            }
        }

        private static Stream<Entry> filterStream(Stream<Entry> stream, Set<Filter<?>> filters) {
            if (filters == null) return stream;
            for (Filter<?> filter : filters) {
                Predicate<Entry> p = getFilter(filter);
                stream = stream.filter(p);
            }
            return stream;
        }

        @NotNull
        private static Predicate<Entry> getFilter(Filter<?> filter) {
            if (filter.isComplexFilter()) return complexFilter(filter);
            return simpleFilter(filter);
        }

        private static Predicate<Entry> complexFilter(Filter<?> filter) {
            if (!filter.operator().equals("=")) throw new IllegalArgumentException("1");
            return e -> ((Collection<String>) filter.value()).stream().anyMatch(f -> getStringEqFilter(filter.attribute(), f).test(e));
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
            if (value instanceof Float) return getFloatEqFilter(attribute, (float) value);
            throw new IllegalStateException();
        }

        private static Predicate<Entry> getFloatEqFilter(Attribute attribute, float value) {
            return e -> switch (attribute) {
                case Rating -> e.rating() == value;
                default -> throw new IllegalArgumentException("1");
            };
        }

        private static Predicate<Entry> getStringEqFilter(Attribute filterBy, String s) {
            return e -> switch (filterBy) {
                case Name -> Objects.equals(e.name(), s);
                case WritingStatus -> Objects.equals(e.writingStatus(), WritingStatus.getStatus(s));
                case ReadingStatus -> Objects.equals(e.readingStatus(), ReadingStatus.getStatus(s));
                default -> throw new IllegalArgumentException("1");
            };
        }

        @NotNull
        private static Predicate<Entry> getUneqFilter(Filter<?> filter) {
            return e -> switch (filter.attribute()) {
                case ReadTo -> getFloatUneqFilter(filter.operator(), (Float) filter.value(), e.readto());
                case Rating -> getFloatUneqFilter(filter.operator(), (Float) filter.value(), e.rating());
                case LastRead -> getDateUneqFilter(filter.operator(), (LocalDate) filter.value(), e.lastread());
                case WaitUntil -> getDateUneqFilter(filter.operator(), (LocalDate) filter.value(), e.waituntil());
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
            daf.stream().map(Attribute::getFunction).forEach(tds::add);
            rr.setDatasupplier(tds);
        }
    }
}
