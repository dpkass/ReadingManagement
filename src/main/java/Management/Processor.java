package Management;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryNotFoundException;
import Management.Processors.*;

import java.util.List;

class Processor {

    static List<String> out;
    static EntryList el;

    static void doAdd(String[] parts) {
        if (parts.length < 4) throw new IllegalArgumentException("1");

        Entry e = getEntry(parts[2]);

        out.add(Adder.add(e, parts));
    }

    static void doChange(String[] parts) {
        if (parts.length < 4) throw new IllegalArgumentException("1");

        Entry e = getEntry(parts[2]);

        out.add(Changer.change(e, parts));
    }

    static void doList(String[] parts) {
        out.addAll(Lister.list(parts, el.entries().stream()));
    }

    static void doListAll() {
        out.addAll(Lister.listAll(el.entries().stream()));
    }

    public static void doShow(String[] parts) {
        Entry e = getEntry(parts[1]);
        out.addAll(Shower.show(e, parts));
    }

    static void doNew(String[] parts) {
        if (parts.length < 2) throw new IllegalArgumentException("1");
        Entry e = el.get(parts[1]);
        if (e != null) throw new IllegalArgumentException("2");
        out.add(Newer.make(el, parts));
    }

    public static void doOpen(String[] parts) {
        if (parts.length != 2) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts[1]);
        Opener.open(e);
    }

    static void doRead(String[] parts) {
        if (parts.length != 3) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts[1]);
        out.add(Reader.read(e, parts[2]));
    }

    static void doReadTo(String[] parts) {
        if (parts.length != 3) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts[1]);
        out.add(Reader.readto(e, parts[2]));
    }

    private static Entry getEntry(String part) {
        Entry e = el.get(part);
        if (e == null) throw new EntryNotFoundException("3");
        return e;
    }
}
