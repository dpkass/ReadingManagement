package Management;

import AppRunner.Datastructures.Error;
import EntryHandling.Entry.EntryList;
import Processing.Processor;
import Processing.RequestResult;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProcessStarter {

    final EntryList el;
    final EntryList secretel;
    RequestResult rr;

    public ProcessStarter(EntryList el, EntryList secretel, RequestResult rr) {
        this.el = el;
        this.secretel = secretel;
        this.rr = rr;
        Processor.setRr(rr);
    }

    void process(String s) {
        if (s == null || s.isBlank()) return;
        
        Processor.setEl(el);
        List<String> parts = Pattern.compile("([^\"]\\S*|\".*\")\\s*")
                                    .matcher(s)
                                    .results()
                                    .map(str -> str.group(1))
                                    .collect(Collectors.toList());

        process(parts);
    }

    void process(List<String> parts) {
        try {
            switch (Helper.representation(parts.get(0))) {
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
                case "h" -> doHelp(parts);
                default -> throw new IllegalArgumentException("1");
            }
        } catch (Exception e) {
            int code = Integer.parseInt(e.getMessage());
            String message = Helper.errorMessage(e.getMessage());
            rr.setError(new Error(code, message));
        }
    }

    private void doHelp(List<String> parts) {
        rr.setType(RequestResult.RequestResultType.HELP);
        rr.setString(Helper.help(parts));
    }

    void doSecret(List<String> parts) {
        parts = parts.subList(1, parts.size());
        Processor.setEl(secretel);
        process(parts);
    }
}