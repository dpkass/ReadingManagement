package EntryHandling.Entry;

import EntryHandling.Entry.Status.ReadingStatus;
import EntryHandling.Entry.Status.WritingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntryBuilder {
    String name;
    float readto = 0f;
    float rating = -1f;
    String link = "";
    WritingStatus ws;
    ReadingStatus rs;

    LocalDateTime lastread = LocalDateTime.now();
    LocalDate pauseduntil = LocalDate.now();

    List<String> abbreviations = new ArrayList<>();

    public EntryBuilder(List<String> values) {
        name = values.get(0);
        if (values.size() > 1) readto = parseReadTo(values.get(1));
        if (values.size() > 2) link = values.get(2);
        if (values.size() > 3) ws = WritingStatus.getStatus(values.get(3));
        if (values.size() > 4) lastread = toLDT(values.get(4));
        if (values.size() > 5) abbreviations.addAll(values.subList(5, values.size()));
        chooseStatus();
    }

    public EntryBuilder(Map<String, Object> JSONmap) {
        for (Map.Entry<String, Object> entry : JSONmap.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> name = (String) entry.getValue();
                case "readto" -> readto = ((BigDecimal) entry.getValue()).floatValue();
                case "rating" -> rating = ((BigDecimal) entry.getValue()).floatValue();
                case "link" -> link = (String) entry.getValue();
                case "lastread" -> lastread = toLDT((String) entry.getValue());
                case "pauseduntil" -> pauseduntil = toLD((String) entry.getValue());
                case "writingStatus" -> ws = WritingStatus.getStatus((String) entry.getValue());
                case "readingStatus" -> rs = ReadingStatus.getStatus((String) entry.getValue());
                case "abbreviations" -> abbreviations = (List<String>) entry.getValue();
            }
        }
        chooseStatus();
    }

    private void chooseStatus() {
        if (rs == ReadingStatus.Paused && pauseduntil == null) rs = ReadingStatus.Reading;      // autonullify paused when date arrives
        if (rs != ReadingStatus.Default) return;

        double rt = readto;
        if (rt == 0) {
            rs = ReadingStatus.NotStarted;
            lastread = null;
        } else if (rt <= 5) rs = ReadingStatus.Started;
        else rs = ReadingStatus.Paused;
    }

    private LocalDateTime toLDT(String value) {
        if (value == null) return null;
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException dtpe) {
            return LocalDateTime.now();
        }
    }

    private LocalDate toLD(String value) {
        if (value == null) return null;
        try {
            LocalDate ld = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            return ld.isBefore(LocalDate.now()) ? null : ld;
        } catch (DateTimeParseException dtpe) {
            return null;
        }
    }

    private float parseReadTo(String rt) {
        try {
            return Float.parseFloat(rt);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("4");
        }
    }

    public Entry toEntry() {
        return new Entry(name, readto, link, rating, ws, rs, lastread, pauseduntil, abbreviations);
    }
}
