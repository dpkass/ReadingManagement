package EntryHandling;

public interface FileHandler {
    EntryList read();

    void write(EntryList el);
}
