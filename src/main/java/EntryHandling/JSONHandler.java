package EntryHandling;

import AppRunner.Datacontainers.FileNotValidException;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class JSONHandler implements FileHandler {

    final boolean secret;
    File file;

    public JSONHandler(File file, boolean secret) {
        this.secret = secret;
        this.file = file;
    }

    @Override
    public EntryList read() {
        EntryList list = new EntryList();

        if (file.length() == 0) write(list);
        else try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String jsonStr;
            if (secret) {
                byte[] decoded = Base64.getDecoder().decode(bytes);
                jsonStr = new String(decoded);
            } else jsonStr = new String(bytes);

            JSONObject json = new JSONObject(jsonStr);
            list.addAll(json.getJSONArray("books").toList());
        } catch (IllegalArgumentException | IOException | JSONException e) {
            throw new FileNotValidException(file.getName(), secret);
        }

        return list;
    }

    @Override
    public void write(EntryList el) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            byte[] bytes = EntryUtil.toJSON(el).getBytes();
            if (secret) bytes = Base64.getEncoder().encode(bytes);
            out.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setFile(File f) {
        this.file = f;
    }
}
