package EntryHandling;

public record Entry(String name, int readto, String link) {
    public Entry(String[] values) {
        this(values[0], Integer.parseInt(values[1]), values[2]);
    }

    public Entry plusRead(int i) {
        return new Entry(name, readto + i, link);
    }

    public String toCSV() {
        return name + ", " + readto + ", " + link;
    }
}
