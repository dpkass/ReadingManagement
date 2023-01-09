package Processing.Modifier;

import AppRunner.Datacontainers.Booktype;
import AppRunner.Datacontainers.ChangeForm;
import AppRunner.Datacontainers.Genre;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.WritingStatus;
import Processing.RequestResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static Processing.Processor.rr;

public class Modifier {

    public static void change(Entry e, ChangeForm changeform) {
        String res = Changer.change(e, changeform);
        finish(res);
    }

    public static void new_(EntryList el, String book, float readto, String link, WritingStatus ws, LocalDateTime lastread, Booktype booktype,
                            List<Genre> genres) {
        String res = Newer.new_(el, book, readto, link, ws, lastread, booktype, genres);
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
