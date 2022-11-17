package Management.Processors;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
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

        Map<Boolean, List<String>> type0filterMap = parts.stream()
                                                         .skip(1)
                                                         .map(Helper::representation)
                                                         .distinct()
                                                         .collect(Collectors.partitioningBy(s -> s.matches(".*[=<>].*")));
        Function<Entry, String> f = getListType(type0filterMap.get(false));
        Comparator<Entry> sorter = getSortingType(type0filterMap.get(true));
        if (parts.size() > 2) entrystream = filterStream(type0filterMap.get(true), entrystream);

        return entrystream.sorted(sorter).map(f).toList();
    }

    private static Comparator<Entry> getSortingType(List<String> filterList) {
        String sb = filterList.stream()
                              .filter(s -> s.startsWith("sb") || s.startsWith("sortBy") || s.startsWith("sortby") || s.startsWith("sort"))
                              .findAny()
                              .orElse(null);
        if (sb == null) return Comparator.comparing(Entry::lastread);
        else {
            filterList.remove(sb);
            String[] parts = sb.split("=");
            Comparator<Entry> comp = switch (Helper.representation(parts[1])) {
                case "r" -> Comparator.comparing(Entry::readto);
                case "n" -> Comparator.comparing(Entry::name);
                case "lr" -> Comparator.comparing(Entry::lastread);
                case "ws" -> Comparator.comparingInt(e -> EntryUtil.statusOrdinal(e.writingStatus()));
                case "rs" -> Comparator.comparingInt(e -> EntryUtil.statusOrdinal(e.readingStatus()));
                default -> throw new IllegalArgumentException();
            };

            if (parts.length > 2 && Helper.representation(parts[2]).equals("desc")) return comp.reversed();
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
                default -> throw new RuntimeException();
            };
            default -> e -> switch (Helper.representation(filter[0])) {
                case "lr" -> getLastReadFilter(filter, e);
                case "r" -> getReadFilter(filter, e);
                default -> throw new RuntimeException();
            };
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
            if (Objects.equals(filter[1], "<")) return Double.parseDouble(e.readto()) < Double.parseDouble(filter[2]);
            else return Double.parseDouble(e.readto()) > Double.parseDouble(filter[2]);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("1");
        }
    }

    @NotNull
    private static Function<Entry, String> getListType(List<String> filterList) {
        return e -> e.name() + " --> " + String.join(", ", filterList.stream().map((f) -> getListValue(e, f)).toList());
    }

    private static String getListValue(Entry e, String filter) {
        return switch (Helper.representation(filter)) {
            case "n" -> e.name();
            case "lk" -> e.link();
            case "lr" -> e.lastread().format(dtf);
            case "ws" -> Objects.toString(e.writingStatus());
            case "rs" -> Objects.toString(e.readingStatus());
            case "r", "rt" -> e.readto();
            case "ab" -> e.abbreviations().toString();
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static Collection<String> listAll(Stream<Entry> stream) {
        return stream.map(Entry::toString).collect(Collectors.toSet());
    }
}
