package EntryHandling.Entry;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EntryUtil {

    private static final int codingOffset = 1;

    // checker
    public static boolean hasAbbreviation(Entry e, String s) {
        return e.abbreviations().contains(s);
    }

    // representations
    public static String asCSV(Entry e) {
        return "%s, %s, %s, %s, %s, %s, [%s]".formatted(e.name(), e.readto(), e.link(), e.writingStatus(), e.readingStatus(), e.lastread()
                                                                                                                               .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")),
                String.join(", ",
                        e.abbreviations()));
    }

    public static String asJSON(Entry e) {
        return """
               "name": "%s",
               "readto": "%s",
               "link": "%s",
               "lastread": "%s",
               "abbreviations": ["%s"]""".formatted(e.name(), e.readto(), e.link(), e.lastread()
                                                                                     .format(DateTimeFormatter.ofPattern("dd MMM yyyy, " +
                                                                                             "HH:mm")),
                String.join("\", \"", e.abbreviations()));
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

    static double doubleValue(String... read) throws ParseException {
        double ret = 0;
        for (String r : read) {
            Number rtVal = NumberFormat.getInstance(Locale.US).parse(r);
            ret += rtVal.doubleValue();
        }
        return ret;
    }

    public static int statusOrdinal(Status s) {
        return s.ordinal();
    }
}
