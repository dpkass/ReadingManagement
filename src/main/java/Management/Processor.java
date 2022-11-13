package Management;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryNotFoundException;
import Management.Processors.*;

import java.util.ArrayList;
import java.util.List;

class Processor {

    static final List<String> out = new ArrayList<>();
    static EntryList el;

    static void doAdd(List<String> parts) {
        if (parts.size() < 4) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(2));
        out.add(Adder.add(el.entries(), e, parts));
    }

    static void doChange(List<String> parts) {
        if (parts.size() < 4) throw new IllegalArgumentException("1");

        Entry e = getEntry(parts.get(2));

        out.add(Changer.change(e, parts));
    }

    static void doList(List<String> parts) {
        out.addAll(Lister.list(parts, el.entries()));
    }

    static void doListAll() {
        out.addAll(Lister.listAll(el.entries()));
    }

    public static void doShow(List<String> parts) {
        Entry e = getEntry(parts.get(1));
        out.addAll(Shower.show(e, parts));
    }

    static void doNew(List<String> parts) {
        if (parts.size() < 2) throw new IllegalArgumentException("1");
        Entry e = el.get(parts.get(1));
        if (e != null) throw new IllegalArgumentException("2");
        out.add(Newer.make(el, parts));
    }

    public static void doOpen(List<String> parts) {
        if (parts.size() != 2) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(1));
        Opener.open(e);
    }

    static void doRead(List<String> parts) {
        if (parts.size() != 3) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(1));
        out.add(Reader.read(e, parts.get(2)));
    }

    static void doReadTo(List<String> parts) {
        if (parts.size() != 3) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(1));
        out.add(Reader.readto(e, parts.get(2)));
    }

    private static Entry getEntry(String part) {
        Entry e = el.get(part);
        if (e == null) throw new EntryNotFoundException("3");
        return e;
    }
}
