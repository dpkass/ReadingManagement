package Management.Processors;

import EntryHandling.Entry.EntryList;

import java.util.List;

public class Newer {

    public static String make(EntryList el, List<String> parts) {
        try {
            List<String> vals = parts.subList(1, parts.size());
            el.add(vals);
            return "Entry added.";
        } catch (Exception e) {
            throw new IllegalArgumentException("1");
        }
    }
}
