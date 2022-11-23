package Management.Processors;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import Management.Helper;
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

import static Management.Helper.df;
import static Management.Helper.dtf;

public class Lister {

    public static List<String> list(List<String> parts, Stream<Entry> entrystream) {
        if (parts.size() == 1) return entrystream.map(Entry::name).toList();
        // remove command and duplicates and distinguish filters/sort/categorize from elements to list
        Map<Boolean, List<String>> type0filterMap = parts.stream()
                                                         .skip(1)
                                                         .map(Helper::representation)
                                                         .distinct()
                                                         .collect(Collectors.partitioningBy(s -> s.matches(".*[=<>].*")));
        // get printing function
        Function<Entry, String> f = getListType(type0filterMap.get(false));
        // split sorting and categorizing commands in a map
        Map<String, String[]> orderMap = getOrderStrings(type0filterMap.get(true));

        Comparator<Entry> sorter = getSorter(orderMap.get("sb"));
        entrystream = filterStream(type0filterMap.get(true), entrystream);

        return print(entrystream, f, orderMap, sorter);
    }

    @NotNull
    private static List<String> print(Stream<Entry> entrystream, Function<Entry, String> f, Map<String, String[]> orderMap, Comparator<Entry> sorter) {
        // if not grouped print normally
        String[] obs = orderMap.get("gb");
        if (obs == null) return entrystream.sorted(sorter).map(f).toList();

        Map<Object, List<Entry>> categorizedLists = categorizetoMaps(entrystream, obs);
        return categorizedPrint(categorizedLists, sorter, f);
    }

    private static List<String> categorizedPrint(Map<Object, List<Entry>> categorizedLists, Comparator<Entry> sorter, Function<Entry, String> f) {
        List<String> res = new ArrayList<>();

        List<Object> categories = categorizedLists.keySet().stream().sorted().toList();
        for (Object o : categories) {
            res.add(o + ":");
            categorizedLists.get(o).stream().sorted(sorter).map(f).map(str -> "   " + str).forEach(res::add);
        }
        return res;
    }

    private static Map<Object, List<Entry>> categorizetoMaps(Stream<Entry> entrystream, String[] categorizeArr) {
        return entrystream.collect(Collectors.groupingBy(e -> switch (Helper.representation(categorizeArr[1])) {
            case "ws" -> e.writingStatus();
            case "rs" -> e.readingStatus();
            case "r" -> (int) (e.readto()) / 50 * 50;
            case "lr" -> EntryUtil.dateString(e.lastread(), DateTimeFormatter.ofPattern("yyyy-MM"), "-");
            default -> throw new IllegalArgumentException("1");
        }));
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
                case "n" -> Comparator.comparing(Entry::name);
                case "lr" -> Comparator.comparing(Entry::lastread);
                case "pu" -> Comparator.comparing(Entry::pauseduntil);
                case "ws" -> Comparator.comparing(e -> EntryUtil.statusOrdinal(e.writingStatus()));
                case "rs" -> Comparator.comparing(e -> EntryUtil.statusOrdinal(e.readingStatus()));
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
            case "r" -> getReadFilter(filter, e);
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

    private static boolean getReadFilter(String[] filter, Entry e) {
        try {
            if (Objects.equals(filter[1], "<")) return e.readto() < Double.parseDouble(filter[2]);
            else return e.readto() > Double.parseDouble(filter[2]);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("1");
        }
    }

    @NotNull
    private static Function<Entry, String> getListType(List<String> typeList) {
        return e -> e.name() + (typeList.size() == 0 ? "" : " --> " + String.join(", ", typeList.stream()
                                                                                                .map((f) -> getListValue(e, f))
                                                                                                .toList()));
    }

    private static String getListValue(Entry e, String filter) {
        return switch (Helper.representation(filter)) {
            case "n" -> e.name();
            case "lk" -> e.link();
            case "lr" -> EntryUtil.dateString(e.lastread(), dtf, "Not Set");
            case "pu" -> EntryUtil.dateString(e.pauseduntil(), df, "Not Set");
            case "ws" -> Objects.toString(e.writingStatus());
            case "rs" -> Objects.toString(e.readingStatus());
            case "r", "rt" -> EntryUtil.tryIntConversion(e.readto());
            case "ab" -> e.abbreviations().toString();
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static Collection<String> listAll(Stream<Entry> stream) {
        return stream.map(Entry::toString).collect(Collectors.toSet());
    }
}
