package EntryHandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Entry {
    String name;
    int readto;
    String link;

    List<String> acronyms;

    private static final int codingOffset = 20;

    public Entry(String[] values) {
        name = values[0];
        if (values.length > 1) readto = Integer.parseInt(values[1]);
        else readto = 0;
        if (values.length > 2) link = values[2];
        else link = "";
        if (values.length > 3)
            acronyms = Arrays.stream(values).skip(3).collect(Collectors.toCollection(ArrayList::new));
        else acronyms = new ArrayList<>();
    }

    // getter
    public String name() {
        return name;
    }

    public int readto() {
        return readto;
    }

    public String link() {
        return link;
    }

    public List<String> acronyms() {
        return acronyms;
    }

    // checker
    public boolean hasAcronym(String s) {
        return acronyms.contains(s);
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setReadto(int readto) {
        this.readto = readto;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void addAcronym(String acronym) {
        if (!acronyms.contains(acronym)) acronyms.add(acronym);
    }

    public void removeAcronym(String acronym) {
        acronyms.remove(acronym);
    }

    // crypto
    void encode() {
        name = encode(name);
        link = encode(link);
        acronyms = acronyms.stream().map(this::encode).collect(Collectors.toList());
    }

    private String encode(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            sb.append((char) (s.charAt(i) + codingOffset));
        return sb.toString();
    }

    void decode() {
        name = decode(name);
        link = decode(link);
        acronyms = acronyms.stream().map(this::decode).collect(Collectors.toList());
    }

    private String decode(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            sb.append((char) (s.charAt(i) - codingOffset));
        return sb.toString();
    }

    // representations
    public String toCSV() {
        StringBuilder s = new StringBuilder();
        s.append(name);
        s.append(", ").append(readto);
        s.append(", ").append(link);
        s.append(", ").append(String.join(", ", acronyms));
        return s.toString();
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
