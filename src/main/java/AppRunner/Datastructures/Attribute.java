package AppRunner.Datastructures;

import java.util.List;

public enum Attribute {
    name("Name"), readto("Page/Chapter"), link("Link"), rating("Rating"), writingStatus("Writing Status"), readingStatus("Reading Status"), lastread("Last Read"), pauseduntil("Paused Until"), abbreviations("Abbreviations");

    final String displayvalue;

    Attribute(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public static List<Attribute> changingOptions() {
        return List.of(name, link, rating, writingStatus, readingStatus);
    }

    public static List<Attribute> sortingOptions() {
        return List.of(name, readto, rating, lastread, pauseduntil, writingStatus, readingStatus);
    }

    public static List<Attribute> groupingOptions() {
        return List.of(readto, rating, lastread, writingStatus, readingStatus);
    }

    public static String representation(String s) {
        return switch (s) {
            case "name", "n" -> name.displayvalue;
            case "read", "r" -> readto.displayvalue;
            case "link", "lk" -> link.displayvalue;
//            case "secret", "s" -> "s";
            case "rating", "rtg" -> rating.displayvalue;
            case "lastread", "lr" -> lastread.displayvalue;
            case "pauseduntil", "pu" -> pauseduntil.displayvalue;
            case "abbreviation", "ab" -> abbreviations.displayvalue;
            case "reading-status", "readingstatus", "rs" -> readingStatus.displayvalue;
            case "writing-status", "writingstatus", "ws" -> writingStatus.displayvalue;
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static Attribute getAttribute(String s) {
        try {
            return valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    public String displayvalue() {
        return displayvalue;
    }
}
