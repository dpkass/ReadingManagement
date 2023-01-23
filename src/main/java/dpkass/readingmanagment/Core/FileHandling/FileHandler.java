package dpkass.readingmanagment.Core.FileHandling;

import dpkass.readingmanagment.Persistence.EntryRepository;

import java.io.File;

public interface FileHandler {
    EntryRepository read();

    void write(EntryRepository el);

    void setFile(File f);
}
