package AppRunner;

import AppRunner.Datastructures.Operator;
import AppRunner.Datastructures.Request;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.WritingStatus;
import Management.Manager;
import Processing.RequestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RequestController {
    static RequestResult rr = new RequestResult();
    static Manager mgr = new Manager();


    static {
        mgr.setRr(rr);
        mgr.init();
        mgr.start();
    }

    @GetMapping ("/form")
    public String showForm(Model m) {
        buildForm(m);
        return "form";
    }

    @GetMapping ("/commandline")
    public String showCommandline(Model m) {
        buildForm(m);
        return "commandline";
    }

    @PostMapping ("/form")
    public String processForm(Request rq, Model m) {
        mgr.process(rq.asCommand());

        buildForm(m);
        m.addAttribute("request", rq);

        if (rr.hasError()) displayError(m);
        else insertResult(m);
        rr.clear();

        return "form";
    }

    @PostMapping ("/commandline")
    public String processCommandline(String command, Model m) {
        mgr.process(command);

        buildForm(m);
        m.addAttribute("request", command);

        if (rr.hasError()) displayError(m);
        else insertResult(m);
        rr.clear();

        return "commandline";
    }

    private void insertResult(Model m) {
        m.addAttribute("requestresult", rr.copy());
    }

    private void displayError(Model m) {
        m.addAttribute("error", rr.error());
    }

    private void buildForm(Model m) {
        m.addAttribute("operators", Operator.values());
        m.addAttribute("writing_statuses", WritingStatus.values());
        m.addAttribute("books", mgr.entries().map(Entry::name).collect(Collectors.toList()));
        m.addAttribute("changing_options", List.of("n", "rtg", "lk", "pu", "ws", "rs", "ab"));
        m.addAttribute("sorting_options", List.of("r", "lk", "rtg", "lr", "pu", "ws", "rs", "ab"));
        m.addAttribute("grouping_options", List.of("r", "rtg", "lr", "ws", "rs"));
    }
}
