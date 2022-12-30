package EntryHandling;

import AppRunner.Datacontainers.FileNotValidException;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryUtil;
import Management.Manager;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.IntStream;

public class JSONHandler implements FileHandler {

    boolean fileCreated = false;
    final boolean secret;
    final int codingOffset = 1;
    JSONObject json;
    File f;

    public JSONHandler(File f, boolean secret) {
        this.secret = secret;
        this.f = f;
    }

    @Override
    public EntryList read() {
        EntryList list = new EntryList();

        if (!fileCreated && !createFile()) return list;

        try {
            FileReader fr = new FileReader(f);

            IntStream.Builder builder = IntStream.builder();
            while (fr.ready()) builder.add(fr.read());
            IntStream chars = builder.build();

            fr.close();
            // secret files to be decoded
            if (secret) chars = decode(chars);

            try {
                String revert = chars.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
                json = new JSONObject(revert);
            } catch (IllegalArgumentException | JSONException e) {
                throw new FileNotValidException(f.getName(), secret);
            }

            list.addAll(json.getJSONArray("books").toList());
        } catch (IOException | JSONException ignored) {}
        return list;
    }

    private IntStream decode(IntStream chars) {
        return chars.map(i -> i - codingOffset);
    }

    @Override
    public void write(EntryList el) {
        if (!fileCreated && !createFile()) return;

        try {
            PrintWriter pw = new PrintWriter(f);

            // encode secret
            IntStream chars = EntryUtil.toJSON(el).chars();
            if (secret) chars = encode(chars);

            pw.println(buildWriteString(chars));
            pw.close();
        } catch (IOException ignored) {}
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
