package EntryHandling;

import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryUtil;
import Management.Manager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.stream.Collectors;

public class JSONHandler implements FileHandler {

    boolean fileCreated = false;
    JSONObject json;
    File f;

    public JSONHandler(File f) {
        this.f = f;
    }

    @Override
    public EntryList read() {
        EntryList list = new EntryList();

        if (!fileCreated && !createFile()) return list;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            json = new JSONObject(br.lines().collect(Collectors.joining()));
            br.close();

            list.addAll(json.getJSONArray("books").toList());
        } catch (IOException | JSONException ignored) {}
        return list;
    }

    @Override
    public void write(EntryList el) {
        if (!fileCreated && !createFile()) return;

        try {
            PrintWriter pw = new PrintWriter(f);
            pw.println(el.list().stream()
                         .map(EntryUtil::asJSON)
                         .collect(Collectors.joining(",\n", "{\n  \"books\": [\n", "\n  ]\n}")));
            pw.close();
        } catch (IOException ignored) {}
    }

    @Override
    public void setFile(File f) {
        this.f = f;
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
