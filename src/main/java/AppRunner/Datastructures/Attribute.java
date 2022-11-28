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

    public static Attribute representation(String s) {
        return switch (s) {
            case "name", "n" -> name;
            case "read", "r" -> readto;
            case "link", "lk" -> link;
//            case "secret", "s" -> "s";
            case "rating", "rtg" -> rating;
            case "lastread", "lr" -> lastread;
            case "pauseduntil", "pu" -> pauseduntil;
            case "abbreviation", "ab" -> abbreviations;
            case "reading-status", "readingstatus", "rs" -> readingStatus;
            case "writing-status", "writingstatus", "ws" -> writingStatus;
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
