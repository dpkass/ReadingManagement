package Management.Processors;

import EntryHandling.Entry.Entry;
import Management.Helper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static Management.Helper.dtf;

public class Shower {
    public static Collection<String> show(Entry e, List<String> parts) {
        if (parts.size() == 2) return Collections.singleton(e.toString());

        return parts.stream().skip(2).map(s -> switch (Helper.representation(s)) {
            case "r", "rt" -> "read-to: %s".formatted(e.readto());
            case "lk" -> "link: %s".formatted(e.link());
            case "lr" -> "lastread: %s".formatted(e.lastread().format(dtf));
            case "ws" -> "writing-status: %s".formatted(e.writingStatus());
            case "rs" -> "reading-status: %s".formatted(e.readingStatus());
            case "ab" -> "abbreviations: %s".formatted(e.abbreviations().toString());
            default -> throw new IllegalStateException("Unexpected value: " + s);
        }).toList();
    }
}
