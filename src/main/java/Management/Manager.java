package Management;

import EntryHandling.CSVHandler;
import EntryHandling.Entry;
import EntryHandling.EntryList;
import EntryHandling.FileHandler;
import IOHandling.IOHandler;
import IOHandling.StdIOHandler;

import java.io.File;
import java.util.Arrays;

public class Manager {
    static File standardfile = new File("resources/reading");
    File f;

    IOHandler in;
    FileHandler fh;
    EntryList el;

    public Manager() {
        f = standardfile;
        init();
    }

    public void run() {
        for (; ; ) {
            String s = in.read();
            if (!process(s)) break;
        }
    }

    private boolean process(String s) {
        assert s != null;
        String[] parts = s.split(" ");
        switch (parts[0]) {
            case "exit" -> {return false;}
            case "read" -> doRead(parts);
            case "readto" -> doReadTo(parts);
            case "new" -> doNew(parts);
            case "add" -> doAdd(parts);
        }
        return true;
    }

    private void doAdd(String[] parts) {
        if (parts.length < 2) errorMessage("add");

        switch (parts[1]) {
            case "link" -> doAddLink(parts);
            case "acronym", "anym" -> doAddAcronym(parts);
        }
    }

    private void doAddAcronym(String[] parts) {
        if (parts.length < 3 || parts.length > 4) errorMessage("add acronym");

        Entry e = el.get(parts[2]);
        if (e == null) errorMessage("enf");

        e.addAcronym(parts[3]);
    }

    private void doAddLink(String[] parts) {
        if (parts.length < 3 || parts.length > 4) errorMessage("add link");

        Entry e = el.get(parts[2]);
        if (e == null) errorMessage("enf");

        e.setLink(parts[3]);
    }

    private void doNew(String[] parts) {
        if (parts.length < 2) errorMessage("new");

        String[] vals = Arrays.stream(parts).skip(1).toArray(String[]::new);
        el.add(vals);
    }

    private void doReadTo(String[] parts) {
        if (parts.length != 3) errorMessage("readto");

        Entry e = el.get(parts[1]);
        if (e == null) errorMessage("enf");

        e.setReadto(Integer.parseInt(parts[2]));
    }

    private void doRead(String[] parts) {
        if (parts.length != 3) errorMessage("read");

        Entry e = el.get(parts[1]);
        if (e == null) errorMessage("enf");

        e.setReadto(e.readto() + Integer.parseInt(parts[2]));
    }

    private void init() {
        in = new StdIOHandler();
        fh = new CSVHandler(f);
        el = fh.read();
    }


    public void setFile(String f) {
        this.f = new File(f);
        init();
    }

    private void errorMessage(String error) {
        switch (error) {
            case "read", "readto" ->
                    in.write("Invalid Input. Please write \"" + error + "\", name or acronym of the book (in quotes if more than one " +
                            "word) and the page or readto number. E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "add acronym", "add link" ->
                    in.write("Invalid Input. Please write \"" + error + "\", the name or acronym of the book (in quotes if more than one word) and " +
                            "the " + error.split(" ")[1] + ". E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "new" ->
                    in.write("Invalid Input. Please write \"new\", the name or acronym of the book (in quotes if more than one word) (additional: " +
                            "the page or readto number and link  to website). E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "enf" -> in.write("The given book was not found. If you want to add a new Entry use \"new\".");
        }
    }
}
