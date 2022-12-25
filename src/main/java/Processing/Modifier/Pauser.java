package Processing.Modifier;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.ReadingStatus;
import EntryHandling.Entry.WritingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pauser {

    public static String wait(Entry e, LocalDate ld) {
        e.setWaituntil(ld);
        e.setReadingStatus(ReadingStatus.Waiting);
        return "Reading Status & Wait-Until changed";
    }

    public static String pause(Entry e) {
        e.setReadingStatus(ReadingStatus.Paused);
        return "Reading Status changed";
    }
}
