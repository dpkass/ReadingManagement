package Processing.Modifier;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.ReadingStatus;

import java.time.LocalDateTime;

class Reader {
    static String read(Entry e, float read) {
        e.addRead(read);
        e.setLastread(LocalDateTime.now());
        e.setReadingStatus(ReadingStatus.Reading);
        return "Read-to changed.";
    }

    static String readto(Entry e, float readto) {
        e.setReadto(readto);
        e.setLastread(LocalDateTime.now());
        e.setReadingStatus(ReadingStatus.Reading);
        return "Read-to changed.";
    }
}
