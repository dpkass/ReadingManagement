package EntryHandling.Entry;

public class EntryNotFoundException extends RuntimeException {
    String message;

    public EntryNotFoundException() {
    }

    public EntryNotFoundException(String message) {
        this.message = message;
    }

    public EntryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntryNotFoundException(Throwable cause) {
        super(cause);
    }
}
