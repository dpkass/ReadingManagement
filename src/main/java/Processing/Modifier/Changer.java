package Processing.Modifier;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import Management.Helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

class Changer {
    static String change(Entry e, List<String> parts) {
        switch (Helper.representation(parts.get(1))) {
            case "n" -> e.setName(parts.get(3));
            case "rtg" -> {
                float f = Float.parseFloat(parts.get(3));
                if (EntryUtil.checkRating(f)) e.setRating(f);
            }
            case "lk" -> e.setLink(parts.get(3));
            case "pu" -> e.setPauseduntil(toLD(parts.get(3)));
            case "ws" -> e.setWritingStatus(parts.get(3));
            case "rs" -> changeReadingStatus(e, parts);
            default -> throw new IllegalArgumentException("1");
        }
        return "Entry changed.";
    }

    private static void changeReadingStatus(Entry e, List<String> parts) {
        e.setReadingStatus(parts.get(3));
        if (parts.get(3).equals("Paused") || parts.get(3).equals("Waiting"))
            if (parts.size() > 4)
                e.setPauseduntil(LocalDate.parse(parts.get(4), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            else e.setPauseduntil(LocalDate.now().plusWeeks(4));
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
