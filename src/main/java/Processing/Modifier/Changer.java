package Processing.Modifier;

import AppRunner.Datacontainers.Attribute;
import EntryHandling.Entry.Entry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Changer {
    public static String change(Entry e, Attribute attribute, String changevalue) {
        switch (attribute) {
            case Name -> e.setName(changevalue);
            case Rating, StoryRating, CharactersRating, DrawingRating -> setRating(attribute, e, changevalue);
            case Link -> e.setLink(changevalue);
            case WritingStatus -> e.setWritingStatus(changevalue);
            case Booktype -> e.setBooktype(changevalue);
            default -> throw new IllegalArgumentException("1");
        }
        return attribute.displayvalue() + " changed.";
    }

    private static void setRating(Attribute attribute, Entry e, String changevalue) {
        float f = Float.parseFloat(changevalue);
        switch (attribute) {
            case Rating -> e.setRating(f);
            case StoryRating -> e.setStoryrating(f);
            case CharactersRating -> e.setCharactersrating(f);
            case DrawingRating -> e.setDrawingrating(f);
        }

    }

    private static LocalDate toLD(String value) {
        try {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException dtpe) {
            throw new IllegalArgumentException("1");
        }
    }
}
