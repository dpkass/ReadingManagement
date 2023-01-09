package AppRunner.Data.Exceptions;

import java.util.List;

public class RequestParsingException extends RuntimeException {

    List<String[]> errors;
    String command;

    public RequestParsingException(List<String[]> errors, String command) {
        this.errors = errors;
        this.command = command;
    }

    public RequestParsingException(List<String[]> errors) {
        this.errors = errors;
    }

    public List<String[]> errors() {
        return errors;
    }

    public String command() {
        return command;
    }
}
