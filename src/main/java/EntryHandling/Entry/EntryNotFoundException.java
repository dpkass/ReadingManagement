package EntryHandling.Entry;

public class EntryNotFoundException extends RuntimeException {
    String message;

    public EntryNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
