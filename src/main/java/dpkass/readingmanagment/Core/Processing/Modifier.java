package dpkass.readingmanagment.Core.Processing;

import dpkass.readingmanagment.Domain.Aggregates.Book;
import dpkass.readingmanagment.Domain.Aggregates.RequestParts.ChangeForm;
import dpkass.readingmanagment.Domain.Aggregates.RequestResult;
import dpkass.readingmanagment.Domain.Types.*;
import dpkass.readingmanagment.Persistence.EntryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Modifier {

    public static void change(Book e, ChangeForm changeform) {
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

    public static void new_(EntryRepository el, String book, float readto, String link, WritingStatus ws, LocalDateTime lastread, Booktype booktype,
                            List<Genre> genres) {
        String result;
        try {
            el.add(book, readto, link, ws, lastread, booktype, genres);
            result = "Book added.";
        } catch (Exception e) {
            throw new IllegalArgumentException("1");
        }
        finish(result);
    }

    public static void read(Book e, float r) {
        e.addRead(r);
        e.setLastread(LocalDateTime.now());
        e.setReadingStatus(ReadingStatus.Reading);
        finish("Read-to changed.");
    }

    public static void readto(Book e, float rt) {
        e.setReadto(rt);
        e.setLastread(LocalDateTime.now());
        e.setReadingStatus(ReadingStatus.Reading);
        finish("Read-to changed.");
    }

    public static void wait(Book e, LocalDate ldt) {
        e.setWaituntil(ldt);
        e.setReadingStatus(ReadingStatus.Waiting);
        finish("Reading Status & Wait-Until changed");
    }

    public static void pause(Book e) {
        e.setReadingStatus(ReadingStatus.Paused);
        finish("Reading Status changed");
    }

    private static void finish(String res) {
        Processor.rr.setString(res);
        Processor.rr.setType(RequestResult.RequestResultType.SUCCESS);
    }
}
