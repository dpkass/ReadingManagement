package EntryHandling;

import EntryHandling.Entry.EntryList;

import java.io.File;

public interface FileHandler {
    EntryList read();

    void write(EntryList el);

    void setFile(File f);
}
