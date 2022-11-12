package Management.Processors;

import EntryHandling.Entry.Entry;
import Management.Helper;

public class Changer {
    public static void change(Entry e, String[] parts) {
        switch (Helper.representation(parts[1])) {
            case "n" -> e.setName(parts[3]);
            case "lk" -> e.setLink(parts[3]);
            case "ws" -> e.setWritingStatus(parts[3]);
            case "rs" -> e.setReadingStatus(parts[3]);
            case "ab" -> {
                e.removeAbbreviation(parts[2]);
                e.addAbbreviation(parts[3]);
            }
            default -> throw new IllegalArgumentException("invalid");
        }
    }
}
