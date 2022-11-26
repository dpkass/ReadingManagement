package EntryHandling.Entry;

public enum ReadingStatus {

    Default("-"), NotStarted("Not Started"), Started("Started"), Reading("Reading"), Waiting("Waiting"), Paused("Paused"), Done(
            "Done");

    final String displayvalue;

    @Override
    public String toString() {
        return "ReadingStatus{" +
                "displayvalue='" + displayvalue + '\'' +
                '}';
    }

    ReadingStatus(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public String displayvalue() {
        return displayvalue;
    }

    public static ReadingStatus getStatus(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return Default;
        }
    }
}
