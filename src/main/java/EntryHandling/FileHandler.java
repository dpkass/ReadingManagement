package EntryHandling;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileHandler {
    EntryList read() throws IOException;

    void write(EntryList el) throws FileNotFoundException;
}
