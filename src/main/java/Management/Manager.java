package Management;

import EntryHandling.CSVHandler;
import EntryHandling.Entry.EntryList;
import EntryHandling.FileHandler;
import IOHandling.IOHandler;
import IOHandling.StdIOHandler;

import java.io.File;

public class Manager {
    public static File standardfile = new File("resources/index.csv");
    public static File standardsecretfile = new File("resources/secret.csv");

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

    private void init() {
        io = new StdIOHandler();
        fh = new CSVHandler(file);
        secretfh = new CSVHandler(secretfile);
    }

    public void run() {
        start();
        while (process(io.read())) {}
        end();
    }

    private void start() {
        el = fh.read();
        secretel = secretfh.read();
        secretel.decode();
        ps = new ProcessStarter(el, secretel, io);
    }

    private void end() {
        fh.write(el);
        secretel.encode();
        secretfh.write(secretel);
        io.write("Bye :)");
    }

    public boolean process(String s) {
        return ps.process(s);
    }

    public void setFile(String f) {
        file = new File(f);
        fh.setFile(file);

        start();
    }

    public void setSecretfile(String f) {
        secretfile = new File(f);
        secretfh.setFile(secretfile);

        start();
    }
}
