package Processing.Displayer;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import Management.Helper;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static Management.Helper.df;
import static Management.Helper.dtf;

public class DisplayUtil {

    public static Stream<String> allAttributes() {
        // rework
        return Stream.of("r", "rtg", "lk", "lr", "pu", "ws", "rs", "ab");
    }

    static Function<Entry, String> getFunction(String filter) {
        return e -> switch (Helper.representation(filter)) {
            case "lk" -> e.link();
            case "r" -> EntryUtil.tryIntConversion(e.readto());
            case "rtg" -> EntryUtil.tryIntConversion(e.rating());
            case "lr" -> EntryUtil.dateString(e.lastread(), dtf, "Not Set");
            case "pu" -> EntryUtil.dateString(e.pauseduntil(), df, "Not Set");
            case "ws" -> Objects.toString(e.writingStatus());
            case "rs" -> Objects.toString(e.readingStatus());
            case "ab" -> e.abbreviations().toString();
            default -> throw new IllegalArgumentException("1");
        };
    }
}
