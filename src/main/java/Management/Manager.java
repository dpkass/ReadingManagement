package Management;

import EntryHandling.Entry.EntryList;
import EntryHandling.FileHandler;
import EntryHandling.JSONHandler;
import IOHandling.IOHandler;
import IOHandling.StdIOHandler;
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
    private final boolean greeted = false;


    public Manager() {
        file = standardfile;
        secretfile = standardsecretfile;
        init();
    }

    private void init() {
        io = new StdIOHandler();
        fh = new JSONHandler(file, false);
        secretfh = new JSONHandler(secretfile, true);
    }


    public void run() {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (process(io.read())) {}
        end();
    }

    private void start() {
        io.write("Hi :)");
        el = fh.read();
        secretel = secretfh.read();
        ps = new ProcessStarter(el, secretel, io);
    }

    private void end() {
        fh.write(el);
        secretfh.write(secretel);
        io.write("Bye :(");
    }

    public void processSingle(String s) {
        start();
        process(s);
    }

    public boolean process(String s) {
        return ps.process(s);
    }

    public void setFile(String f) {
        file = new File(f);
        fh = new JSONHandler(file, false);
    }

    public void setSecretfile(String f) {
        secretfile = new File(f);
        secretfh = new JSONHandler(secretfile, true);
    }
}
