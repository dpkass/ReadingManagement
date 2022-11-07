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
    public static File standardfile = new File("resources/index.json");
    public static File standardsecretfile = new File("resources/secret.json");

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
        fh = switch (filetype(file)) {
            default -> new JSONHandler(file);
        };
        secretfh = switch (filetype(secretfile)) {
            default -> new JSONHandler(secretfile);
        };
    }


    public void run() {
        start();
        while (process(io.read())) {}
        end();
    }

    private void start() {
        io.write("Hi :)");
        el = fh.read();
        secretel = secretfh.read();
        secretel.decode();
        ps = new ProcessStarter(el, secretel, io);
    }

    private void end() {
        fh.write(el);
        secretel.encode();
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
        fh = new JSONHandler(file);
    }

    public void setSecretfile(String f) {
        secretfile = new File(f);
        secretfh = new JSONHandler(secretfile);
    }

    public String filetype(File file) {
        String filename = file.getName();
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
