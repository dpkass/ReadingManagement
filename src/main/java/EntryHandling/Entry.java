package EntryHandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Entry {
    String name;
    int readto;
    String link;
    List<String> acronyms;

    public Entry(String[] values) {
        this(values[0]);
        if (values.length > 1) readto = Integer.parseInt(values[1]);
        if (values.length > 2) link = values[2];
        if (values.length > 3)
            acronyms = Arrays.stream(values).skip(3).collect(Collectors.toCollection(ArrayList::new));
    }

    public Entry(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public int readto() {
        return readto;
    }

    public String link() {
        return link;
    }

    public boolean hasAcronym(String s) {
        return acronyms.contains(s);
    }

    public void setReadto(int readto) {
        this.readto = readto;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void addAcronym(String acronym) {
        acronyms.add(acronym);
    }

    public String toCSV() {
        return name + ", " + readto + ", " + link;
    }
}
