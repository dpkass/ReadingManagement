package EntryHandling.Entry;

public enum WritingStatus {
    Default("Not Set"),
    ComingUp("Coming-up"),
    Rolling("Rolling"),
    Paused("Paused"),
    Ended("Ended");

    final String displayvalue;

    WritingStatus(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public String displayvalue() {
        return displayvalue;
    }

    public static WritingStatus getStatus(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return Default;
        }
    }
}
