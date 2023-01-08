package EntryHandling.Entry;

public class EntryNotFoundException extends RuntimeException {
    int code;

    public EntryNotFoundException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
