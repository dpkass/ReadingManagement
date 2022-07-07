package EntryHandling;

import java.io.*;
import java.util.Arrays;

public class CSVHandler implements FileHandler {
    static String standardSeparator = ";";

    String separator;
    File f;

    public CSVHandler(File f) {
        this.f = f;
        separator = standardSeparator;
    }

    @Override
    public EntryList read() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        EntryList list = new EntryList();
        br.lines()
          .map(a -> Arrays.stream(a.split(separator))
                          .map(String::stripLeading).toArray(String[]::new))
          .forEach(list::add);
        br.close();
        return list;
    }

    @Override
    public void write(EntryList el) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(f);
        el.list.stream()
               .map(Entry::toCSV)
               .map(a -> a.replace(",", separator))
               .forEach(pw::println);
        pw.close();
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
