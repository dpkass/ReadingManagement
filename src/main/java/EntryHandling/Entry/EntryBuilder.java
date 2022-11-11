package EntryHandling.Entry;

import EntryHandling.Entry.Status.ReadingStatus;
import EntryHandling.Entry.Status.WritingStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EntryBuilder {
    String name;
    String readto = "0";
    String link = "";
    WritingStatus ws;
    ReadingStatus rs;

    LocalDateTime lastread = LocalDateTime.now();

    List<String> abbreviations = new ArrayList<>();

    public EntryBuilder(String[] values) {
        name = values[0];
        if (values.length > 1) readto = values[1];
        if (values.length > 2) link = values[2];
        if (values.length > 3) ws = WritingStatus.getStatus(values[3]);
        if (values.length > 4) lastread = toLDT(values[4]);
        if (values.length > 5)
            Arrays.stream(values).skip(5).forEach(abbreviations::add);
        chooseStatus();
    }

    public EntryBuilder(Map<String, Object> JSONmap) {
        for (Map.Entry<String, Object> entry : JSONmap.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> name = (String) entry.getValue();
                case "readto" -> readto = (String) entry.getValue();
                case "link" -> link = (String) entry.getValue();
                case "lastread" -> lastread = toLDT((String) entry.getValue());
                case "writingStatus" -> ws = WritingStatus.getStatus((String) entry.getValue());
                case "readingStatus" -> rs = ReadingStatus.getStatus((String) entry.getValue());
                case "abbreviations" -> abbreviations = (List<String>) entry.getValue();
            }
        }
        chooseStatus();
    }

    private void chooseStatus() {
        if (rs != ReadingStatus.Default) return;

        double rt = Double.parseDouble(readto);
        if (rt == 0) rs = ReadingStatus.NotStarted;
        else if (rt <= 5) rs = ReadingStatus.Started;
        else rs = ReadingStatus.Paused;
    }

    private LocalDateTime toLDT(String value) {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException dtpe) {
            return LocalDateTime.now();
        }
    }

    public Entry toEntry() {
        return new Entry(name, readto, link, ws, rs, lastread, abbreviations);
    }
}
