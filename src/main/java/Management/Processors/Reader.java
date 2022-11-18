package Management.Processors;

import EntryHandling.Entry.Entry;

import java.time.LocalDateTime;

public class Reader {
    public static String read(Entry e, String read) {
        try {
            e.addRead(Double.parseDouble(read));
            e.setLastread(LocalDateTime.now());
            return "Read-to changed.";
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("4");
        }
    }

    public static String readto(Entry e, String read) {
        try {
            e.setReadto(Double.parseDouble(read));
            e.setLastread(LocalDateTime.now());
            return "Read-to changed.";
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("4");
        }
    }
}
