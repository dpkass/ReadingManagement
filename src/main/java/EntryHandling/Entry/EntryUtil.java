package EntryHandling.Entry;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.stream.Collectors;

public class EntryUtil {

    private static final int codingOffset = 1;

    // checker
    public static boolean hasAbbreviation(Entry e, String s) {
        return e.abbreviations().contains(s);
    }

    // crypto
    static void encode(Entry e) {
        e.setName(encode(e.name()));
        e.setLink(encode(e.link()));
        e.setReadto(encode(e.readto()));
        e.setAbbreviations(e.abbreviations().stream().map(EntryUtil::encode).collect(Collectors.toList()));
    }

    private static String encode(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            sb.append((char) (s.charAt(i) + codingOffset));
        return sb.toString();
    }

    static void decode(Entry e) {
        e.setName(decode(e.name()));
        e.setLink(decode(e.link()));
        e.setReadto(decode(e.readto()));
        e.setAbbreviations(e.abbreviations().stream().map(EntryUtil::decode).collect(Collectors.toList()));
    }

    private static String decode(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            sb.append((char) (s.charAt(i) - codingOffset));
        return sb.toString();
    }

    // representations
    public static String asCSV(Entry e) {
        return "%s, %s, %s, %s".formatted(e.name(), e.readto(), e.link(), String.join(", ", e.abbreviations()));
    }

    public static String asJSON(Entry e) {
        return """
               {
                 "name": "%s",
                 "readto": "%s",
                 "link": "%s",
                 "abbreviations": ["%s"]
               }""".formatted(e.name(), e.readto(), e.link(), String.join("\", \"", e.abbreviations()));
    }

    static double doubleValue(String... read) throws ParseException {
        double ret = 0;
        for (String r : read) {
            Number rtVal = NumberFormat.getInstance(Locale.US).parse(r);
            ret += rtVal.doubleValue();
        }
        return ret;
    }
}
