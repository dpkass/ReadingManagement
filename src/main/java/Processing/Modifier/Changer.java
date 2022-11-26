package Processing.Modifier;

import AppRunner.Datastructures.Attribute;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Changer {
    public static String change(Entry e, Attribute attribute, String changevalue) {
        switch (attribute) {
            case name -> e.setName(changevalue);
            case rating -> {
                float f = Float.parseFloat(changevalue);
                if (EntryUtil.checkRating(f)) e.setRating(f);
            }
            case link -> e.setLink(changevalue);
            case pauseduntil -> e.setPauseduntil(toLD(changevalue));
            case writingStatus -> e.setWritingStatus(changevalue);
            case readingStatus -> changeReadingStatus(e, changevalue);
            default -> throw new IllegalArgumentException("1");
        }
        return attribute.displayvalue() + " changed.";
    }

    private static void changeReadingStatus(Entry e, String changevalue) {
        e.setReadingStatus(changevalue);
        if (changevalue.equals("Paused") || changevalue.equals("Waiting"))
//            rework if change rs to be able to change pu
//            if (parts.size() > 4)
//                e.setPauseduntil(LocalDate.parse(parts.get(4), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
//            else
            e.setPauseduntil(LocalDate.now().plusWeeks(4));
        else e.setPauseduntil(null);
    }

    private static LocalDate toLD(String value) {
        try {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException dtpe) {
            throw new IllegalArgumentException("1");
        }
    }
}
