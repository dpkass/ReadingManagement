package Processing.Modifier;

import AppRunner.Datacontainers.Booktype;
import AppRunner.Datacontainers.Genre;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.WritingStatus;

import java.time.LocalDateTime;
import java.util.List;

class Newer {

    public static String new_(EntryList el, String book, float newpagevalue, String newlinkvalue, WritingStatus newwsvalue,
                              LocalDateTime newlrvalue, Booktype booktype, List<Genre> genres) {
        try {
            el.add(book, newpagevalue, newlinkvalue, newwsvalue, newlrvalue, booktype, genres);
            return "Entry added.";
        } catch (Exception e) {
            throw new IllegalArgumentException("1");
        }
    }
}
