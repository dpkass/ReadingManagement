package Management.Processors;

import EntryHandling.Entry.Entry;
import Management.Helper;

import java.util.List;

public class Changer {
    public static String change(Entry e, List<String> parts) {
        switch (Helper.representation(parts.get(1))) {
            case "n" -> e.setName(parts.get(3));
            case "lk" -> e.setLink(parts.get(3));
            case "ws" -> e.setWritingStatus(parts.get(3));
            case "rs" -> e.setReadingStatus(parts.get(3));
            case "ab" -> {
                e.removeAbbreviation(parts.get(2));
                e.addAbbreviation(parts.get(3));
            }
            default -> throw new IllegalArgumentException("1");
        }
        return "Entry changed.";
    }
}
