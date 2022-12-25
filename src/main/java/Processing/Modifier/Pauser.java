package Processing.Modifier;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.ReadingStatus;

import java.time.LocalDate;

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
