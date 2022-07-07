package EntryHandling;

public class Entry {
    String name;
    int readto;
    String link;

    public Entry(String[] values) {
        this(values[0], Integer.parseInt(values[1]), values[2]);
    }

    public Entry(String name, int readto, String link) {
        this.name = name;
        this.readto = readto;
        this.link = link;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setReadto(int readto) {
        this.readto = readto;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toCSV() {
        return name + ", " + readto + ", " + link;
    }
}
