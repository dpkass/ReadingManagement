package Management;

import EntryHandling.Entry.EntryList;
import IOHandling.IOHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessStarter {

    EntryList el;
    EntryList secretel;
    IOHandler io;

    public ProcessStarter(EntryList el, EntryList secretel, IOHandler io) {
        this.el = el;
        this.secretel = secretel;
        this.io = io;
    }

    boolean process(String s) {
        if (s == null || s.isBlank()) return true;

        String[] parts = split(s);

        switch (Helper.representation(parts[0])) {
            case "s" -> Processor.el = secretel;
            default -> Processor.el = el;
        }
        Processor.out = new ArrayList<>();

        boolean b = process(parts);

        Processor.out.forEach(io::write);

        return b;
    }

    boolean process(String[] parts) {
        switch (Helper.representation(parts[0])) {
            case "e" -> {return false;}
            case "nw" -> Processor.doNew(parts);
            case "r" -> Processor.doRead(parts);
            case "rt" -> Processor.doReadTo(parts);
            case "a" -> Processor.doAdd(parts);
            case "c" -> Processor.doChange(parts);
            case "l" -> Processor.doList(parts);
            case "sh" -> Processor.doShow(parts);
            case "la" -> Processor.doListAll();
            case "o" -> Processor.doOpen(parts);
            case "s" -> doSecret(parts);
            case "h" -> io.write(Helper.help(parts));
            default -> io.write(Helper.errorMessage("invalid"));
        }
        return true;
    }

    void doSecret(String[] parts) {
        parts = Arrays.stream(parts).skip(1).toArray(String[]::new);
        process(parts);
    }

    private static String[] split(String command) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find()) list.add(m.group(1).replace("\"", ""));
        return list.toArray(String[]::new);
    }
}