package EntryHandling;

import java.io.*;
import java.util.Arrays;

public class CSVHandler implements FileHandler {
    static String standardSeparator = ";";

    String separator;
    File f;

    public CSVHandler(File f) {
        this.f = f;
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        separator = standardSeparator;
    }

    @Override
    public EntryList read() {
        EntryList list = new EntryList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            br.lines()
              .map(a -> Arrays.stream(a.split(separator))
                              .map(String::stripLeading).toArray(String[]::new))
              .forEach(list::add);
            br.close();
        } catch (IOException ignored) {}
        return list;
    }

    @Override
    public void write(EntryList el) {
        try {
            PrintWriter pw = new PrintWriter(f);
            el.list.stream()
                   .map(Entry::toCSV)
                   .map(a -> a.replace(",", separator))
                   .forEach(pw::println);
            pw.close();
        } catch (IOException ignored) {}
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
