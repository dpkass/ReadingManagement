package AppRunner.Datacontainers;

import java.util.List;

public enum Attribute {
    name("Name"), readto("Page/Chapter"), link("Link"), rating("Rating"), writingStatus("Writing Status"),
    readingStatus("Reading Status"), lastread("Last Read"), waituntil("Wait Until"), abbreviations("Abbreviations");

    final String displayvalue;

    Attribute(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public static List<Attribute> changingOptions() {
        return List.of(name, link, rating, writingStatus);
    }

    public static List<Attribute> sortingOptions() {
        return List.of(name, readto, rating, lastread, waituntil, writingStatus, readingStatus);
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
            case "waituntil", "wu" -> waituntil;
            case "abbreviation", "ab" -> abbreviations;
            case "reading-status", "readingstatus", "rs" -> readingStatus;
            case "writing-status", "writingstatus", "ws" -> writingStatus;
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static Attribute getAttribute(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException iae) {
            try {
                return representation(s);
            } catch (IllegalArgumentException iae2) {
                return null;
            }
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public String displayvalue() {
        return displayvalue;
    }
}
