package EntryHandling.Entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntryBuilder {
    String name;
    String readto;
    String link;

    List<String> abbreviations;

    public EntryBuilder(String[] values) {
        name = values[0];
        if (values.length > 1) readto = values[1];
        else readto = String.valueOf(0);
        if (values.length > 2) link = values[2];
        else link = "";
        if (values.length > 3)
            abbreviations = Arrays.stream(values).skip(3).collect(Collectors.toCollection(ArrayList::new));
        else abbreviations = new ArrayList<>();
    }

    public EntryBuilder(Map<String, Object> JSONmap) {
        for (Map.Entry<String, Object> entry : JSONmap.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> name = (String) entry.getValue();
                case "readto" -> readto = (String) entry.getValue();
                case "link" -> link = (String) entry.getValue();
                case "abbreviations" -> abbreviations = (List<String>) entry.getValue();
            }
        }
    }

    public Entry toEntry() {
        return new Entry(name, readto, link, abbreviations);
    }
}
