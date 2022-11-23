package EntryHandling.Entry;

public enum WritingStatus {
    Default,
    ComingUp,
    Rolling,
    Paused,
    Ended;

    public String toString() {
        return switch (this) {
            case Default -> "Not set";
            case ComingUp -> "Coming-up";
            default -> super.toString();
        };
    }

    public static WritingStatus getStatus(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return Default;
        }
    }
}
