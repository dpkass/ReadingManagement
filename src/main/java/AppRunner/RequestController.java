package AppRunner;

import AppRunner.Datastructures.Attribute;
import AppRunner.Datastructures.Operator;
import AppRunner.Datastructures.Request;
import AppRunner.Datastructures.RequestDummy;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.WritingStatus;
import Management.Manager;
import Processing.RequestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
        m.addAttribute("request", RequestDummy.standard());
        return "form";
    }

    @GetMapping ("/commandline")
    public String showCommandline(Model m) {
        m.addAttribute("command", "List");
        return "commandline";
    }

    @PostMapping ("/form")
    public String processForm(RequestDummy rq, Model m) {
        mgr.process(rq.toRequest());

        buildForm(m);
        m.addAttribute("request", rq);

        if (rr.hasError()) displayError(m);
        else insertResult(m);
        rr.clear();

        return "form";
    }

    @PostMapping ("/commandline")
    public String processCommandline(String command, Model m) {
        Request rq = Request.parse(command);
        mgr.process(rq);

        m.addAttribute("command", command);

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
        m.addAttribute("operators", Operator.formoperators());
        m.addAttribute("writing_statuses", WritingStatus.values());
        m.addAttribute("books", mgr.entries().map(Entry::name).collect(Collectors.toList()));
        m.addAttribute("changing_options", Attribute.changingOptions());
        m.addAttribute("sorting_options", Attribute.sortingOptions());
        m.addAttribute("grouping_options", Attribute.groupingOptions());
    }
}
