package Processing.Modifier;

import AppRunner.Datacontainers.Attribute;
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
            case writingStatus -> e.setWritingStatus(changevalue);
            default -> throw new IllegalArgumentException("1");
        }
        return attribute.displayvalue() + " changed.";
    }

    private static LocalDate toLD(String value) {
        try {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException dtpe) {
            throw new IllegalArgumentException("1");
        }
    }
}
