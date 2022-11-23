package Management;

import EntryHandling.Entry.EntryList;
import EntryHandling.FileHandler;
import EntryHandling.JSONHandler;
import IOHandling.IOHandler;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Manager {
    public static final File standardfile = new File("resources/index.json");
    public static final File standardsecretfile = new File("resources/secret.json");

    File file;
    File secretfile;

    IOHandler io;
    FileHandler fh;
    FileHandler secretfh;
    EntryList el;
    EntryList secretel;

    ProcessStarter ps;

    public Manager() {
        file = standardfile;
        secretfile = standardsecretfile;
        init();
    }

    public void init() {
        fh = new JSONHandler(file, false);
        secretfh = new JSONHandler(secretfile, true);
    }


    public void run() {
        start();
        while (process(io.read())) {}
        end();
    }

    public void start() {
        el = fh.read();
        secretel = secretfh.read();
        ps = new ProcessStarter(el, secretel, io);
    }

    private void end() {
        fh.write(el);
        secretfh.write(secretel);
    }

    public boolean process(String s) {
        return ps.process(s);
    }

    public void setIo(IOHandler io) {
        this.io = io;
    }
}
