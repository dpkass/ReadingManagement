package Processing;

import AppRunner.Datastructures.Request;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryNotFoundException;
import Processing.Displayer.Displayer;
import Processing.Modifier.Modifier;

import java.util.ArrayList;
import java.util.List;

public class Processor {

    static EntryList el;
    public static RequestResult rr;

    List<String> out = new ArrayList<>();

    public static void doAdd(Request rq) {
        Entry e = getEntry(rq.book());
        Modifier.add(e, rq.addvalue(), el.entries());
    }

    public static void doChange(Request rq) {
        Entry e = getEntry(rq.book());
        Modifier.change(e, rq.changeattribute(), rq.changevalue());
    }

    public static void doList(Request rq) {
        Displayer.list(el.entries(), rq.daf(), rq.filters(), rq.sortby(), rq.groupby());
    }

    public static void doListAll() {
        Displayer.listAll(el.entries());
    }

    public static void doRecommend() {
        Displayer.recommend(el.entries());
    }

    public static void doShow(Request rq) {
        Entry e = getEntry(rq.book());
        Displayer.show(e, rq.daf());
    }

    public static void doNew(Request rq) {
        Entry e = el.get(rq.book());
        if (e != null) throw new IllegalArgumentException("2");
        Modifier.make(el, rq.book(), rq.newpagevalue(), rq.newlinkvalue(), rq.newwsvalue(), rq.newlrvalue());
    }

    public static void doOpen(Request rq) {
        Entry e = getEntry(rq.book());
        Opener.open(e);
    }

    public static void doRead(Request rq) {
        Entry e = getEntry(rq.book());
        Modifier.read(e, rq.readvalue());
    }

    public static void doReadTo(Request rq) {
        Entry e = getEntry(rq.book());
        Modifier.readto(e, rq.readvalue());
    }

    public static Entry getEntry(String part) {
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
