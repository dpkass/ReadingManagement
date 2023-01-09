package AppRunner.Data.Exceptions;

public class FileNotValidException extends RuntimeException {
    String filename;
    boolean secret;

    public FileNotValidException(String name, boolean secret) {
        filename = name;
        this.secret = secret;
    }

    public String filename() {
        return filename;
    }

    public boolean secret() {
        return secret;
    }
}
