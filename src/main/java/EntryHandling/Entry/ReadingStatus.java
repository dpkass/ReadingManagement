package EntryHandling.Entry;

import java.util.List;

public enum ReadingStatus {

    Default("-"), NotStarted("Not Started"), Started("Started"), Reading("Reading"), Waiting("Waiting"), Paused("Paused"), Done("Done");

    final String displayvalue;

    ReadingStatus(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public static List<ReadingStatus> displayableRS() {
        return List.of(NotStarted, Started, Reading, Waiting, Paused, Done);
    }

    public static ReadingStatus getStatus(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return Default;
        }
    }

    public String displayvalue() {
        return displayvalue;
    }
}
