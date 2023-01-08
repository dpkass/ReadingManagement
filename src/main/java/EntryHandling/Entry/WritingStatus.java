package EntryHandling.Entry;

import java.util.List;

public enum WritingStatus {
    Default("-"),
    ComingUp("Coming-up"),
    Rolling("Rolling"),
    Paused("Paused"),
    Ended("Ended");

    public static List<WritingStatus> selectableWS() {
        return List.of(ComingUp, Rolling, Paused, Ended);
    }

    final String displayvalue;

    WritingStatus(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public static WritingStatus getStatus(String name) {
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
