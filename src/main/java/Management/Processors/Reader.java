package Management.Processors;

import EntryHandling.Entry.Entry;

import java.time.LocalDateTime;

public class Reader {
    public static String read(Entry e, String read) {
        if (e.addRead(read)) {
            e.setLastread(LocalDateTime.now());
            return "Read-to changed.";
        } else throw new IllegalArgumentException("4");
    }

    public static String readto(Entry e, String read) {
        e.setReadto(read);
        e.setLastread(LocalDateTime.now());
        return "Read-to changed.";
    }
}
