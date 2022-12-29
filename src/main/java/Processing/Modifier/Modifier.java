package Processing.Modifier;

import AppRunner.Datastructures.Attribute;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.WritingStatus;
import Processing.RequestResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static Processing.Processor.rr;

public class Modifier {

    public static void add(Entry e, String newVal, Stream<Entry> el) {
        String res = Adder.add(e, newVal, el);
        finish(res);
    }

    public static void change(Entry e, Attribute attribute, String changevalue) {
        String res = Changer.change(e, attribute, changevalue);
        finish(res);
    }

    public static void new_(EntryList el, String book, float newpagevalue, String newlinkvalue, WritingStatus newwsvalue, LocalDateTime newlrvalue) {
        String res = Newer.new_(el, book, newpagevalue, newlinkvalue, newwsvalue, newlrvalue);
        finish(res);
    }

    public static void read(Entry e, float r) {
        String res = Reader.read(e, r);
        finish(res);
    }

    public static void readto(Entry e, float rt) {
        String res = Reader.readto(e, rt);
        finish(res);
    }

    public static void wait(Entry e, LocalDate ldt) {
        String res = Pauser.wait(e, ldt);
        finish(res);
    }

    public static void pause(Entry e) {
        String res = Pauser.pause(e);
        finish(res);
    }

    private static void finish(String res) {
        rr.setString(res);
        rr.setType(RequestResult.RequestResultType.SUCCESS);
    }
}
