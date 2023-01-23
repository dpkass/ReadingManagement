package dpkass.readingmanagment.Domain;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class StringConverter {

    public static String dateString(Temporal date, DateTimeFormatter dtf, String alt) {
        return date == null ? alt : dtf.format(date);
    }

    public static String ratingString(float f) {
        if (f == -1) return "-";
        return f == (int) f ? "%d".formatted((int) f) : "%s".formatted(f);
    }
}
