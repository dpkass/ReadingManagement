package EntryHandling.Entry;

public enum ReadingStatus {

    Default("-"), NotStarted("Not Started"), Started("Started"), Reading("Reading"), Waiting("Waiting"), Paused("Paused"), Done("Done");

    final String displayvalue;

    ReadingStatus(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public static ReadingStatus[] displayableRS() {
        return new ReadingStatus[] { NotStarted, Started, Reading, Waiting, Paused, Done };
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
