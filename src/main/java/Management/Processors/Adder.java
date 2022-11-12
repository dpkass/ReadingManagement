package Management.Processors;

import EntryHandling.Entry.Entry;
import Management.Helper;

public class Adder {
    public static void add(Entry e, String[] parts) {
        switch (Helper.representation(parts[1])) {
            case "ab" -> e.addAbbreviation(parts[3]);
            default -> throw new IllegalArgumentException("invalid");
        }
    }
}
