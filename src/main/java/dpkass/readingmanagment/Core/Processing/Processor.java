package dpkass.readingmanagment.Core.Processing;

import dpkass.readingmanagment.Domain.Aggregates.Book;
import dpkass.readingmanagment.Domain.Aggregates.Request;
import dpkass.readingmanagment.Domain.Aggregates.RequestResult;
import dpkass.readingmanagment.Persistence.EntryRepository;
import dpkass.readingmanagment.Domain.Exceptions.EntryNotFoundException;

public class Processor {

    static EntryRepository el;
    public static RequestResult rr;

    public static void doChange(Request rq) {
        Book e = getEntry(rq.booksel());
        Modifier.change(e, rq.changeform());
    }

    public static void doList(Request rq) {
        Displayer.list(el.entries(), rq.daf(), rq.filters(), rq.sortby(), rq.groupby(), rq.sortdescending(), rq.groupdescending());
    }

    public static void doWait(Request rq) {
        Book e = getEntry(rq.booksel());
        Modifier.wait(e, rq.waituntil());
    }

    public static void doPause(Request rq) {
        Book e = getEntry(rq.booksel());
        Modifier.pause(e);
    }

    public static void doRecommend() {
        Displayer.recommend();
    }

    public static void doNew(Request rq) {
        Book e = el.get(rq.booknew());
        if (e != null) throw new IllegalArgumentException("2");
        Modifier.new_(el, rq.booknew(), rq.newpagevalue(), rq.newlinkvalue(), rq.newwsvalue(), rq.newlrvalue(), rq.newbooktypevalue(), rq.newgenresvalue());
    }

    public static void doOpen(Request rq) {
        Book e = getEntry(rq.booksel());
        Opener.open(e.link());
    }

    public static void doRead(Request rq) {
        Book e = getEntry(rq.booksel());
        Modifier.read(e, rq.readvalue());
    }

    public static void doReadTo(Request rq) {
        Book e = getEntry(rq.booksel());
        Modifier.readto(e, rq.readvalue());
    }

    public static Book getEntry(String part) {
        Book e = el.get(part);
        if (e == null) throw new EntryNotFoundException(3);
        return e;
    }

    // setter
    public static void setEl(EntryRepository el) {
        Processor.el = el;
    }

    public static void setRr(RequestResult rr) {
        Processor.rr = rr;
    }
}
