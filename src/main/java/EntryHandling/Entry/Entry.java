package EntryHandling.Entry;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Entry {
    private String name;
    private String readto;
    private String link;
    @JsonFormat (pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime lastread;

    private final List<String> abbreviations;

    public Entry(String name, String readto, String link, LocalDateTime lastread, List<String> abbreviations) {
        this.name = name;
        this.readto = readto;
        this.link = link;
        this.lastread = lastread;
        this.abbreviations = abbreviations;
    }

    // getter
    public String name() {
        return name;
    }

    public String readto() {
        return readto;
    }

    public String link() {
        return link;
    }

    public LocalDateTime lastread() {
        return lastread;
    }

    public List<String> abbreviations() {
        return abbreviations;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setReadto(String rt) {
        try {
            double value = EntryUtil.doubleValue(rt);
            if (value % 1 == 0) readto = String.valueOf((int) value);
            else readto = String.valueOf(value);
        } catch (ParseException ignored) {
            this.readto = rt;
        }
    }

    public boolean addRead(String read) {
        try {
            double value = EntryUtil.doubleValue(readto, read);
            if (value % 1 == 0) readto = String.valueOf((int) value);
            else readto = String.valueOf(value);
            return true;
        } catch (ParseException ignored) {
            return false;
        }
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLastread(LocalDateTime lastread) {
        this.lastread = lastread;
    }

    public void setAbbreviations(List<String> abbreviations) {
        this.abbreviations.clear();
        this.abbreviations.addAll(abbreviations);
    }

    public void addAbbreviation(String abbreviation) {
        if (!abbreviations.contains(abbreviation)) abbreviations.add(abbreviation);
    }

    public void removeAbbreviation(String abbreviation) {
        abbreviations.remove(abbreviation);
    }

    @Override
    public String toString() {
        return EntryUtil.asCSV(this);
    }

    @Override
    public boolean equals(Object obj) {
        return equals((Entry) obj);
    }

    public boolean equals(Entry e) {
        return Objects.equals(e.name, this.name);
    }
}
