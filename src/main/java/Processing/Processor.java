package Processing;

import AppRunner.Datacontainers.Request;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryNotFoundException;
import Processing.Displayer.Displayer;
import Processing.Modifier.Modifier;

public class Processor {

    static EntryList el;
    public static RequestResult rr;

    public static void doAdd(Request rq) {
        Entry e = getEntry(rq.booksel());
        Modifier.add(e, rq.addvalue(), el.entries());
    }

    public static void doChange(Request rq) {
        Entry e = getEntry(rq.booksel());
        Modifier.change(e, rq.changeattribute(), rq.changevalue());
    }

    public static void doList(Request rq) {
        Displayer.list(el.entries(), rq.daf(), rq.filters(), rq.sortby(), rq.groupby(), rq.sortdescending(), rq.groupdescending());
    }

    public static void doWait(Request rq) {
        Entry e = getEntry(rq.booksel());
        Modifier.wait(e, rq.waituntil());
    }

    public static void doPause(Request rq) {
        Entry e = getEntry(rq.booksel());
        Modifier.pause(e);
    }

    public static void doRecommend() {
        Displayer.recommend();
    }

    public static void doNew(Request rq) {
        Entry e = el.get(rq.booknew());
        if (e != null) throw new IllegalArgumentException("2");
        Modifier.new_(el, rq.booknew(), rq.newpagevalue(), rq.newlinkvalue(), rq.newwsvalue(), rq.newlrvalue());
    }

    public static void doOpen(Request rq) {
        Entry e = getEntry(rq.booksel());
        Opener.open(e.link());
    }

    public static void doRead(Request rq) {
        Entry e = getEntry(rq.booksel());
        Modifier.read(e, rq.readvalue());
    }

    public static void doReadTo(Request rq) {
        Entry e = getEntry(rq.booksel());
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
