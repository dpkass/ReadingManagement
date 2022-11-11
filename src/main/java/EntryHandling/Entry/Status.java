package EntryHandling.Entry;

public class Status {
    enum WritingStatus {
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

    enum ReadingStatus {
        Default,
        NotStarted,
        Started,
        Reading,
        Waiting,
        Paused,
        Done;

        public String toString() {
            return switch (this) {
                case Default -> "Not set";
                case NotStarted -> "Not started";
                default -> super.toString();
            };
        }

        public static ReadingStatus getStatus(String name) {
            try {
                return valueOf(name);
            } catch (Exception e) {
                return Default;
            }
        }
    }
}
