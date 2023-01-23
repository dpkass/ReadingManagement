package dpkass.readingmanagment.Domain.Exceptions;

public class EntryNotFoundException extends RuntimeException {
    int code;

    public EntryNotFoundException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
