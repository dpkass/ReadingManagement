package Processing;

import EntryHandling.Entry.Entry;

import java.time.LocalDateTime;

class Reader {
    static String read(Entry e, String read) {
        try {
            e.addRead(Double.parseDouble(read));
            e.setLastread(LocalDateTime.now());
            return "Read-to changed.";
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("4");
        }
    }

    static String readto(Entry e, String read) {
        try {
            e.setReadto(Float.parseFloat(read));
            e.setLastread(LocalDateTime.now());
            return "Read-to changed.";
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("4");
        }
    }
}
