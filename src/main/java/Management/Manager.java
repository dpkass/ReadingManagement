package Management;

import EntryHandling.CSVHandler;
import EntryHandling.EntryList;
import EntryHandling.FileHandler;
import IOHandling.IOHandler;
import IOHandling.StdIOHandler;

import java.io.File;
import java.util.List;

public class Manager {
    public static File standardfile = new File("resources/index.csv");
    File f;

    IOHandler io;
    FileHandler fh;
    EntryList el;

    public Manager() {
        f = standardfile;
        init();
    }

    private void init() {
        io = new StdIOHandler();
        fh = new CSVHandler(f);
        el = fh.read();
    }

    public void run() {
        for (; ; ) {
            String command = io.read();
            if (!process(command)) break;
        }
    }

    public boolean process(String s) {
        List<String> out = Processor.process(s);
        if (out == null) {
            fh.write(el);
            return false;
        }
        out.forEach(io::write);
        return true;
    }


    public void setFile(String f) {
        this.f = new File(f);
        fh.setFile(this.f);
        el = fh.read();
    }
}
