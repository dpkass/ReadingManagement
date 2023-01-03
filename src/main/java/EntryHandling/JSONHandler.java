package EntryHandling;

import AppRunner.Datacontainers.FileNotValidException;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.stream.IntStream;

public class JSONHandler implements FileHandler {

    final boolean secret;
    final int codingOffset = 1;
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
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            IntStream chars = IntStream.generate(bais::read).limit(bais.available());

            if (secret) chars = decode(chars);

            String revert = chars.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            JSONObject json = new JSONObject(revert);
            list.addAll(json.getJSONArray("books").toList());
        } catch (IllegalArgumentException | IOException | JSONException e) {
            throw new FileNotValidException(file.getName(), secret);
        }

        return list;
    }

    private IntStream decode(IntStream chars) {
        return chars.map(i -> i - codingOffset);
    }

    @Override
    public void write(EntryList el) {
        try (PrintWriter pw = new PrintWriter(file)) {

            IntStream chars = EntryUtil.toJSON(el).chars();
            if (secret) chars = encode(chars);

            pw.println(buildWriteString(chars));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private String buildWriteString(IntStream chars) {
        char quotes = (char) ('"' + codingOffset);
        char white = (char) (' ' + codingOffset);
        int crint = 0xa + codingOffset;
        int lfint = 0xd + codingOffset;
        String cr = String.format("\\x%02X", crint);
        String lf = String.format("\\x%02X", lfint);
        String regex = "[%s%s%s]+(?=([^%s]*%s[^%s]*%s)*[^%s]*$)".formatted(white, cr, lf, quotes, quotes, quotes, quotes, quotes);
        return chars.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString().replaceAll(regex, "");
    }

    private IntStream encode(IntStream chars) {
        return chars.map(i -> i + codingOffset);
    }

    @Override
    public void setFile(File f) {
        this.file = f;
    }
}
