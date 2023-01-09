package Processing.Modifier;

import AppRunner.Datacontainers.Attribute;
import AppRunner.Datacontainers.Booktype;
import AppRunner.Datacontainers.ChangeForm;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.WritingStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Changer {
    public static String change(Entry e, ChangeForm changeform) {
        List<String> changed = new ArrayList<>();
        for (Map.Entry<Attribute, Object> entry : changeform.changeMap().entrySet()) {
            switch (entry.getKey()) {
                case Name -> e.setName((String) entry.getValue());
                case Rating, StoryRating, CharactersRating, DrawingRating -> setRating(entry.getKey(), e, (Float) entry.getValue());
                case Link -> e.setLink((String) entry.getValue());
                case WritingStatus -> e.setWritingStatus((WritingStatus) entry.getValue());
                case Booktype -> e.setBooktype((Booktype) entry.getValue());
                default -> throw new IllegalArgumentException("1");
            }
            changed.add(entry.getKey().displayvalue());
        }
        return String.join(", ", changed) + " changed.";
    }

    private static void setRating(Attribute attribute, Entry e, float changevalue) {
        switch (attribute) {
            case Rating -> e.setRating(changevalue);
            case StoryRating -> e.setStoryrating(changevalue);
            case CharactersRating -> e.setCharactersrating(changevalue);
            case DrawingRating -> e.setDrawingrating(changevalue);
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
