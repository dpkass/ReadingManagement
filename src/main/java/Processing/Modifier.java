package Processing;

import AppRunner.Data.Containers.ChangeForm;
import AppRunner.Data.Containers.RequestResult;
import AppRunner.Data.Types.Attribute;
import AppRunner.Data.Types.Booktype;
import AppRunner.Data.Types.Genre;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.ReadingStatus;
import EntryHandling.Entry.WritingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Processing.Processor.rr;

public class Modifier {

    public static void change(Entry e, ChangeForm changeform) {
        List<String> changed = new ArrayList<>();
        for (Map.Entry<Attribute, Object> entry : changeform.changeMap().entrySet()) {
            switch (entry.getKey()) {
                case Name -> e.setName((String) entry.getValue());
                case Rating -> e.setRating((Float) entry.getValue());
                case StoryRating -> e.setStoryrating((Float) entry.getValue());
                case CharactersRating -> e.setCharactersrating((Float) entry.getValue());
                case DrawingRating -> e.setDrawingrating((Float) entry.getValue());
                case Link -> e.setLink((String) entry.getValue());
                case WritingStatus -> e.setWritingStatus((WritingStatus) entry.getValue());
                case Booktype -> e.setBooktype((Booktype) entry.getValue());
                default -> throw new IllegalArgumentException("1");
            }
            changed.add(entry.getKey().displayvalue());
        }
        finish(String.join(", ", changed) + " changed.");
    }

    public static void new_(EntryList el, String book, float readto, String link, WritingStatus ws, LocalDateTime lastread, Booktype booktype,
                            List<Genre> genres) {
        String result;
        try {
            el.add(book, readto, link, ws, lastread, booktype, genres);
            result = "Entry added.";
        } catch (Exception e) {
            throw new IllegalArgumentException("1");
        }
        finish(result);
    }

    public static void read(Entry e, float r) {
        e.addRead(r);
        e.setLastread(LocalDateTime.now());
        e.setReadingStatus(ReadingStatus.Reading);
        finish("Read-to changed.");
    }

    public static void readto(Entry e, float rt) {
        e.setReadto(rt);
        e.setLastread(LocalDateTime.now());
        e.setReadingStatus(ReadingStatus.Reading);
        finish("Read-to changed.");
    }

    public static void wait(Entry e, LocalDate ldt) {
        e.setWaituntil(ldt);
        e.setReadingStatus(ReadingStatus.Waiting);
        finish("Reading Status & Wait-Until changed");
    }

    public static void pause(Entry e) {
        e.setReadingStatus(ReadingStatus.Paused);
        finish("Reading Status changed");
    }

    private static void finish(String res) {
        rr.setString(res);
        rr.setType(RequestResult.RequestResultType.SUCCESS);
    }
}
