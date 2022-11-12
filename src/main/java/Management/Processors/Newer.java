package Management.Processors;

import EntryHandling.Entry.EntryList;

import java.util.Arrays;

public class Newer {

    public static String make(EntryList el, String[] parts) {
        try {
            String[] vals = Arrays.stream(parts).skip(1).toArray(String[]::new);
            el.add(vals);
            return "Entry added.";
        } catch (Exception e) {
            throw new IllegalArgumentException("1");
        }
    }
}
