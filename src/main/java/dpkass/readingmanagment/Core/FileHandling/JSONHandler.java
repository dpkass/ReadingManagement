package dpkass.readingmanagment.Core.FileHandling;

import dpkass.readingmanagment.Domain.Exceptions.FileNotValidException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dpkass.readingmanagment.Persistence.EntryRepository;
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

    public JSONHandler(boolean secret) {
        this.secret = secret;
    }

    @Override
    public EntryRepository read() {
        EntryRepository list = new EntryRepository();

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
    public void write(EntryRepository el) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            String result;
            result = toJSON(el);
            byte[] bytes = result.getBytes();
            if (secret) bytes = Base64.getEncoder().encode(bytes);
            out.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJSON(EntryRepository el) {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        om.registerModule(new JavaTimeModule());
        ObjectWriter ow = om.writerWithDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(el);
        } catch (IOException ioe) {return "";}
    }

    @Override
    public void setFile(File f) {
        this.file = f;
    }
}
