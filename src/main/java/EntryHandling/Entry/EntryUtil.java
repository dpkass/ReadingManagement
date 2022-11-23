package EntryHandling.Entry;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class EntryUtil {

    public static void checkAbbreviations(Stream<Entry> el, String... newAbs) {
        if (el.map(Entry::abbreviations)
              .flatMap(Collection::stream)
              .anyMatch(ab -> Arrays.asList(newAbs).contains(ab))) {
            throw new IllegalArgumentException("7");
        }
    }

    public static boolean checkRating(float f) {
        return f >= 0f && f <= 5f;
    }

    public static String dateString(Temporal date, DateTimeFormatter dtf, String alt) {
        return date == null ? alt : dtf.format(date);
    }

    // checker
    public static boolean hasAbbreviation(Entry e, String s) {
        return e.abbreviations().contains(s);
    }

    // representations
    public static String asCSV(Entry e) {
        return "%s, %s, %s, %s, %s, (%s), (%s), [%s]".formatted(e.name(), e.readto(), e.link(), e.writingStatus(), e.readingStatus(), dateString(e.lastread(), DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"), "-"), dateString(e.pauseduntil(), DateTimeFormatter.ofPattern("dd MMM yyyy"), "-"), String.join(", ", e.abbreviations()));
    }

    public static String asJSON(Entry e) {
        return """
               "name": "%s",
               "readto": "%s",
               "link": "%s",
               "lastread": "%s",
               "readingstatus": "%s",
               "writingstatus": "%s",
               "pauseduntil": "%s",
               "abbreviations": ["%s"]""".formatted(e.name(), e.readto(), e.link(), e.writingStatus(), e.readingStatus(), dateString(e.lastread(), DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"), "-"), dateString(e.pauseduntil(), DateTimeFormatter.ofPattern("dd MMM yyyy"), "-"), String.join(", ", e.abbreviations()));
    }

    public static String toJSON(Object e) {
        ObjectWriter ow = buildOW();
        try {
            return ow.writeValueAsString(e);
        } catch (IOException ioe) {return "";}
    }

    private static ObjectWriter buildOW() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        om.registerModule(new JavaTimeModule());
        return om.writerWithDefaultPrettyPrinter();
    }

    public static int statusOrdinal(Status s) {
        return s.ordinal();
    }

    public static String tryIntConversion(float f) {
        return f == (int) f ? "%d".formatted((int) f) : "%s".formatted(f);
    }
}
