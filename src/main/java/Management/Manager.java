package Management;

import EntryHandling.Entry.EntryList;
import EntryHandling.FileHandler;
import EntryHandling.JSONHandler;
import Processing.RequestResult;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Manager {
    public static final File standardfile = new File("resources/index.json");
    public static final File standardsecretfile = new File("resources/secret.json");

    File file;
    File secretfile;

    RequestResult rr;
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

    public void start() {
        el = fh.read();
        secretel = secretfh.read();
        ps = new ProcessStarter(el, secretel, rr);
    }

    private void save() {
        fh.write(el);
        secretfh.write(secretel);
    }

    public void process(String s) {
        ps.process(s);
        save();
    }

    public void setRr(RequestResult rr) {
        this.rr = rr;
    }
}
