package Processing;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryNotFoundException;
import Processing.Displayer.Displayer;
import Processing.Modifier.Modifier;

import java.util.ArrayList;
import java.util.List;

public class Processor {

    public static final List<String> out = new ArrayList<>();
    static EntryList el;
    public static RequestResult rr;
    
    public static void doAdd(List<String> parts) {
        if (parts.size() < 4) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(2));
        Modifier.add(el.entries(), e, parts);
    }

    public static void doChange(List<String> parts) {
        if (parts.size() < 4) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(2));
        Modifier.change(e, parts);
    }

    public static void doList(List<String> parts) {
        Displayer.list(parts, el.entries());
    }

    public static void doListAll() {
        Displayer.listAll(el.entries());
    }

    public static void doRecommend() {
        Displayer.recommend(el.entries());
    }

    public static void doShow(List<String> parts) {
        if (parts.size() < 2) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(1));
        Displayer.show(e, parts);
    }

    public static void doNew(List<String> parts) {
        if (parts.size() < 2) throw new IllegalArgumentException("1");
        Entry e = el.get(parts.get(1));
        if (e != null) throw new IllegalArgumentException("2");
        Modifier.make(el, parts);
    }

    public static void doOpen(List<String> parts) {
        if (parts.size() != 2) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(1));
        Opener.open(e);
    }

    public static void doRead(List<String> parts) {
        if (parts.size() != 3) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(1));
        Modifier.read(e, parts.get(2));
    }

    public static void doReadTo(List<String> parts) {
        if (parts.size() != 3) throw new IllegalArgumentException("1");
        Entry e = getEntry(parts.get(1));
        Modifier.readto(e, parts.get(2));
    }

    private static Entry getEntry(String part) {
        Entry e = el.get(part);
        if (e == null) throw new EntryNotFoundException("3");
        return e;
    }

    // setter
    public static void setEl(EntryList el) {
        Processor.el = el;
    }

    public static void setRr(RequestResult rr) {
        Processor.rr = rr;
    }
}
