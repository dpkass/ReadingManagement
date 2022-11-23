package EntryHandling.Entry;

public enum ReadingStatus {

    Default, NotStarted, Started, Reading, Waiting, Paused, Done;

    public String toString() {
        return switch (this) {
            case Default -> "Not set";
            case NotStarted -> "Not-Started";
            default -> super.toString();
        };
    }

    public static EntryHandling.Entry.ReadingStatus getStatus(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return Default;
        }
    }
}
