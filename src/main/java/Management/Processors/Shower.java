package Management.Processors;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import Management.Helper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static Management.Helper.df;
import static Management.Helper.dtf;

public class Shower {
    public static Collection<String> show(Entry e, List<String> parts) {
        if (parts.size() == 2) return Collections.singleton(e.toString());

        return parts.stream().skip(2).map(s -> switch (Helper.representation(s)) {
            case "r" -> "read-to: " + EntryUtil.tryIntConversion(e.readto());
            case "rtg" -> "rating: " + EntryUtil.tryIntConversion(e.rating());
            case "lk" -> "link: %s".formatted(e.link());
            case "lr" -> "lastread: %s".formatted(EntryUtil.dateString(e.lastread(), dtf, "-"));
            case "pu" -> "pauseduntil: %s".formatted(EntryUtil.dateString(e.pauseduntil(), df, "-"));
            case "ws" -> "writing-status: %s".formatted(e.writingStatus());
            case "rs" -> "reading-status: %s".formatted(e.readingStatus());
            case "ab" -> "abbreviations: %s".formatted(e.abbreviations().toString());
            default -> throw new IllegalArgumentException("1");
        }).toList();
    }
}
