package EntryHandling.Entry;

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

    LocalDateTime lastread = LocalDateTime.now();

    List<String> abbreviations = new ArrayList<>();

    public EntryBuilder(String[] values) {
        name = values[0];
        if (values.length > 1) readto = values[1];
        else readto = String.valueOf(0);
        if (values.length > 2) link = values[2];
        else link = "";
        if (values.length > 3) lastread = toLDT(values[3]);
        if (values.length > 4)
            Arrays.stream(values).skip(3).forEach(abbreviations::add);
    }

    public EntryBuilder(Map<String, Object> JSONmap) {
        for (Map.Entry<String, Object> entry : JSONmap.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> name = (String) entry.getValue();
                case "readto" -> readto = (String) entry.getValue();
                case "link" -> link = (String) entry.getValue();
                case "lastread" -> lastread = toLDT((String) entry.getValue());
                case "abbreviations" -> abbreviations = (List<String>) entry.getValue();
            }
        }
    }

    private LocalDateTime toLDT(String value) {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException dtpe) {
            return LocalDateTime.now();
        }
    }

    public Entry toEntry() {
        return new Entry(name, readto, link, lastread, abbreviations);
    }
}
