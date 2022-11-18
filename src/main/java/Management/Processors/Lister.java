package Management.Processors;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import EntryHandling.Entry.Status;
import Management.Helper;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Management.Helper.dtf;

public class Lister {

    public static List<String> list(List<String> parts, Stream<Entry> entrystream) {
        if (parts.size() == 1) return entrystream.map(Entry::name).toList();
        // remove command and duplicates and distinuish filters/sort/categorize from elements to list
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

        // if not categorized print normally
        String[] obs = orderMap.get("ob");
        if (obs == null) return entrystream.sorted(sorter).map(f).toList();

        Map<Status, List<Entry>> categorizedLists = categorizetoMaps(entrystream, obs);
        return categorizedPrint(categorizedLists, sorter, f);
    }

    private static List<String> categorizedPrint(Map<Status, List<Entry>> categorizedLists, Comparator<Entry> sorter, Function<Entry, String> f) {
        List<String> res = new ArrayList<>();

        List<Status> categories = categorizedLists.keySet().stream().sorted().toList();
        for (Status s : categories) {
            res.add(s + ":");
            categorizedLists.get(s).stream().sorted(sorter).map(f).map(str -> "   " + str).forEach(res::add);
        }
        return res;
    }

    private static Map<Status, List<Entry>> categorizetoMaps(Stream<Entry> entrystream, String[] categorizeArr) {
        return entrystream.collect(Collectors.groupingBy(e -> switch (Helper.representation(categorizeArr[1])) {
            case "ws" -> e.writingStatus();
            case "rs" -> e.readingStatus();
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
                case "ob" -> duplicate = result.putIfAbsent("ob", parts) != null;
            }
            if (duplicate) throw new IllegalArgumentException("1");
            filterList.remove(s);
        }
        return result;
    }

    @NotNull
    private static Predicate<String> isOrderString() {
        return s -> s.startsWith("sb") || s.startsWith("sortBy") || s.startsWith("sortby") || s.startsWith("sort") || s.startsWith("ob") || s.startsWith("orderBy") || s.startsWith("orderby") || s.startsWith("order");
    }

    private static Comparator<Entry> getSorter(String[] sortArgs) {
        if (sortArgs == null) return Comparator.comparing(Entry::lastread);
        else {
            Comparator<Entry> comp = switch (Helper.representation(sortArgs[1])) {
                case "r" -> Comparator.comparing(Entry::readto);
                case "n" -> Comparator.comparing(Entry::name);
                case "lr" -> Comparator.comparing(Entry::lastread);
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
        return switch (filter[1]) {
            case "=" -> e -> switch (Helper.representation(filter[0])) {
                case "ws" -> Objects.toString(e.writingStatus()).equals(filter[2]);
                case "rs" -> Objects.toString(e.readingStatus()).equals(filter[2]);
                default -> throw new IllegalArgumentException("1");
            };
            case "<", ">" -> e -> switch (Helper.representation(filter[0])) {
                case "lr" -> getLastReadFilter(filter, e);
                case "r" -> getReadFilter(filter, e);
                default -> throw new IllegalArgumentException("1");
            };
            default -> throw new IllegalStateException();
        };
    }

    private static boolean getLastReadFilter(String[] filter, Entry e) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate inputTime = LocalDate.from(LocalDate.parse(filter[2], dtf));
            LocalDate lastRead = LocalDate.from(e.lastread());

            if (Objects.equals(filter[1], "<")) return lastRead.isBefore(inputTime);
            else if (Objects.equals(filter[1], ">")) return lastRead.isAfter(inputTime);
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
    private static Function<Entry, String> getListType(List<String> filterList) {
        return e -> e.name() + (filterList.size() == 0 ? "" : " --> " + String.join(", ", filterList.stream()
                                                                                                    .map((f) -> getListValue(e, f))
                                                                                                    .toList()));
    }

    private static String getListValue(Entry e, String filter) {
        return switch (Helper.representation(filter)) {
            case "n" -> e.name();
            case "lk" -> e.link();
            case "lr" -> e.lastread().format(dtf);
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
