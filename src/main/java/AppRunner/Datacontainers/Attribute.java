package AppRunner.Datacontainers;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Management.Helper.df;
import static Management.Helper.dtf;

public enum Attribute {
    Name,
    Genres,
    Booktype,
    ReadTo("Page/Chapter"),
    LastRead("Last Read"),
    ReadingStatus("Reading Status"),
    WaitUntil("Wait Until"),
    WritingStatus("Writing Status"),
    Rating,
    Link;

    final String displayvalue;

    Attribute() {
        this.displayvalue = this.toString();
    }

    Attribute(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public static List<Attribute> changingOptions() {
        return List.of(Name, Booktype, Rating, WritingStatus, Link);
    }

    public static List<Attribute> displayingOptions() {
        return List.of(Genres, Booktype, ReadTo, LastRead, ReadingStatus, WaitUntil, WritingStatus, Rating, Link);
    }

    public static Function<Entry, String> getFunction(Attribute att) {
        return e -> switch (att) {
            case Link -> e.link();
            case ReadTo -> EntryUtil.tryIntConversion(e.readto());
            case Rating -> EntryUtil.tryIntConversion(e.rating());
            case LastRead -> EntryUtil.dateString(e.lastread(), dtf, "-");
            case WaitUntil -> EntryUtil.dateString(e.waituntil(), df, "-");
            case WritingStatus -> e.writingStatus().displayvalue();
            case ReadingStatus -> e.readingStatus().displayvalue();
            case Genres -> e.genres().stream().map(Genre::displayvalue).collect(Collectors.joining("\n"));
            case Booktype -> e.booktype().displayvalue();
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static List<Attribute> sortingOptions() {
        return List.of(Name, ReadTo, Rating, Booktype, LastRead, WaitUntil, WritingStatus, ReadingStatus);
    }

    public static List<Attribute> groupingOptions() {
        return List.of(ReadTo, Rating, Booktype, LastRead, WritingStatus, ReadingStatus);
    }

    public static Attribute representation(String s) {
        return switch (s) {
            case "name", "n" -> Name;
            case "read", "r" -> ReadTo;
            case "link", "lk" -> Link;
            case "rating", "rtg" -> Rating;
            case "lastread", "lr" -> LastRead;
            case "waituntil", "wu" -> WaitUntil;
            case "reading-status", "readingstatus", "rs" -> ReadingStatus;
            case "writing-status", "writingstatus", "ws" -> WritingStatus;
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static Attribute getAttribute(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException iae) {
            try {
                return representation(s);
            } catch (IllegalArgumentException iae2) {
                return null;
            }
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public String displayvalue() {
        return displayvalue;
    }
}
