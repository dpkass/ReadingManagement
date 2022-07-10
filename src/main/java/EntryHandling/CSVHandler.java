package EntryHandling;

import Management.Manager;

import java.io.*;
import java.util.Arrays;

public class CSVHandler implements FileHandler {
    static String standardSeparator = ";";

    boolean fileCreated = false;
    String separator;
    File f;

    public CSVHandler(File f) {
        this.f = f;
        separator = standardSeparator;
    }

    @Override
    public EntryList read() {
        EntryList list = new EntryList();

        if (!fileCreated && !createFile()) return list;

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
        if (!fileCreated && !createFile()) return;

        try {
            PrintWriter pw = new PrintWriter(f);
            el.list.stream()
                   .map(Entry::toCSV)
                   .map(a -> a.replace(",", separator))
                   .forEach(pw::println);
            pw.close();
        } catch (IOException ignored) {}
    }

    @Override
    public void setFile(File f) {
        this.f = f;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    private boolean createFile() {
        try {
            f.createNewFile();
            fileCreated = true;
            return true;
        } catch (IOException e) {
            if (f != Manager.standardfile) System.out.println("File doesn't exist");
        }
        return false;
    }
}
