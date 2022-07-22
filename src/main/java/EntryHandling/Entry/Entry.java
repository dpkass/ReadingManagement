package EntryHandling.Entry;

import java.util.List;
import java.util.Objects;

public class Entry {
    private String name;
    private String readto;
    private String link;

    private final List<String> acronyms;

    public Entry(String name, String readto, String link, List<String> acronyms) {
        this.name = name;
        this.readto = readto;
        this.link = link;
        this.acronyms = acronyms;
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

    public List<String> acronyms() {
        return acronyms;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setReadto(String readto) {
        this.readto = readto;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setAcronyms(List<String> acronyms) {
        this.acronyms.clear();
        this.acronyms.addAll(acronyms);
    }

    public void addAcronym(String acronym) {
        if (!acronyms.contains(acronym)) acronyms.add(acronym);
    }

    public void removeAcronym(String acronym) {
        acronyms.remove(acronym);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("name=").append(name);
        s.append(", ").append("readto=").append(readto);
        s.append(", ").append("link=").append(link);
        s.append(", ").append("acromnys=[").append(String.join(", ", acronyms)).append("]");
        return s.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return equals((Entry) obj);
    }

    public boolean equals(Entry e) {
        return Objects.equals(e.name, this.name);
    }
}
