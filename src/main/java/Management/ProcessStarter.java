package Management;

import EntryHandling.Entry.EntryList;
import IOHandling.IOHandler;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static Management.Processor.out;

public class ProcessStarter {

    final EntryList el;
    final EntryList secretel;
    final IOHandler io;

    public ProcessStarter(EntryList el, EntryList secretel, IOHandler io) {
        this.el = el;
        this.secretel = secretel;
        this.io = io;
    }

    boolean process(String s) {
        if (s == null || s.isBlank()) return true;

        Processor.el = el;
        List<String> parts = Pattern.compile("([^\"]\\S*|\".*\")\\s*")
                                    .matcher(s)
                                    .results()
                                    .map(str -> str.group(1))
                                    .collect(Collectors.toList());
        boolean b = process(parts);

        out.forEach(io::write);
        out.clear();

        return b;
    }

    boolean process(List<String> parts) {
        try {
            switch (Helper.representation(parts.get(0))) {
                case "e" -> {return false;}
                case "nw" -> Processor.doNew(parts);
                case "r" -> Processor.doRead(parts);
                case "rt" -> Processor.doReadTo(parts);
                case "a" -> Processor.doAdd(parts);
                case "c" -> Processor.doChange(parts);
                case "l" -> Processor.doList(parts);
                case "sh" -> Processor.doShow(parts);
                case "la" -> Processor.doListAll();
                case "rec" -> Processor.doRecommend();
                case "o" -> Processor.doOpen(parts);
                case "s" -> doSecret(parts);
                case "h" -> io.write(Helper.help(parts));
                default -> throw new IllegalArgumentException("1");
            }
        } catch (Exception e) {
            out.add(Helper.errorMessage(e.getMessage()));
        }
        return true;
    }

    void doSecret(List<String> parts) {
        parts = parts.subList(1, parts.size());
        Processor.el = secretel;
        process(parts);
    }
}