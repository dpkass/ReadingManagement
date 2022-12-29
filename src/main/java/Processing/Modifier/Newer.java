package Processing.Modifier;

import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.WritingStatus;

import java.time.LocalDateTime;

class Newer {

    public static String new_(EntryList el, String book, float newpagevalue, String newlinkvalue, WritingStatus newwsvalue,
                              LocalDateTime newlrvalue) {
        try {
            el.add(book, newpagevalue, newlinkvalue, newwsvalue, newlrvalue);
            return "Entry added.";
        } catch (Exception e) {
            throw new IllegalArgumentException("1");
        }
    }
}
