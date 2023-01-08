package Processing.Displayer;

import AppRunner.Datacontainers.Attribute;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;

import java.util.function.Function;

import static Management.Helper.df;
import static Management.Helper.dtf;

public class DisplayUtil {

    static Function<Entry, String> getFunction(Attribute att) {
        return e -> switch (att) {
            case link -> e.link();
            case readto -> EntryUtil.tryIntConversion(e.readto());
            case rating -> EntryUtil.tryIntConversion(e.rating());
            case lastread -> EntryUtil.dateString(e.lastread(), dtf, "Not Set");
            case waituntil -> EntryUtil.dateString(e.waituntil(), df, "Not Set");
            case writingStatus -> e.writingStatus().displayvalue();
            case readingStatus -> e.readingStatus().displayvalue();
            default -> throw new IllegalArgumentException("1");
        };
    }
}
