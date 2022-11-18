package EntryHandling.Entry;

import EntryHandling.Entry.Status.ReadingStatus;
import EntryHandling.Entry.Status.WritingStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

    public EntryBuilder(List<String> values) {
        name = values.get(0);
        if (values.size() > 1) readto = values.get(1);
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
        double rt;
        try {rt = Double.parseDouble(readto);} catch (NumberFormatException e) {throw new RuntimeException("4");}
        return new Entry(name, rt, link, ws, rs, lastread, abbreviations);
    }
}
