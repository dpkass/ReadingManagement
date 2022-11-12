package Management.Processors;

import EntryHandling.Entry.Entry;
import Management.Helper;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Management.Helper.dtf;

public class Lister {

    public static List<String> list(String[] parts, Stream<Entry> stream) {
        if (parts.length == 1) {
            return stream.map(Entry::name).toList();
        }

        Function<Entry, String> f = getListFunction(parts);
        if (parts.length > 2) stream = filterStream(parts, stream);

        return stream.map(e -> e.name() + " --> " + f.apply(e)).toList();
    }

    private static Stream<Entry> filterStream(String[] parts, Stream<Entry> stream) {
        List<String[]> filters =
                Arrays.stream(parts).skip(2).map(filter -> filter.split("((?<=[=<>])|(?=[=<>]))")).toList();
        for (String[] filter : filters) {
            Predicate<Entry> p = getFilter(filter);
            stream = stream.filter(p);
        }
        return stream;
    }

    @NotNull
    private static Predicate<Entry> getFilter(String[] filter) {
        if (filter[1].equals("="))
            return e -> switch (Helper.representation(filter[0])) {
                case "ws" -> Objects.toString(e.writingStatus()).equals(filter[2]);
                case "rs" -> Objects.toString(e.readingStatus()).equals(filter[2]);
                default -> {throw new RuntimeException();}
            };
        else return e -> switch (Helper.representation(filter[0])) {
            case "lr" -> getLastReadFilter(filter, e);
            case "r" -> getReadFilter(filter, e);
            default -> {throw new RuntimeException();}
        };
    }

    private static boolean getLastReadFilter(String[] filter, Entry e) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate inputTime = LocalDate.from(LocalDate.parse(filter[2], dtf));
            LocalDate lastRead = LocalDate.from(e.lastread());

            if (Objects.equals(filter[1], "<"))
                return lastRead.isBefore(inputTime);
            else if (Objects.equals(filter[1], ">"))
                return lastRead.isAfter(inputTime);
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
    private static Function<Entry, String> getListFunction(String[] parts) {
        return e -> switch (Helper.representation(parts[1])) {
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
