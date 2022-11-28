package AppRunner;

import AppRunner.Datastructures.Attribute;
import AppRunner.Datastructures.Operator;
import AppRunner.Datastructures.Request;
import AppRunner.Datastructures.RequestDummy;
import AppRunner.Validation.RequestValidator;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.WritingStatus;
import Management.Manager;
import Processing.RequestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class RequestController {
    Manager mgr;

    public RequestController() {
        mgr = new Manager();

        mgr.init();
        mgr.start();
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(new RequestValidator());
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
    public String processForm(@Valid RequestDummy rq, BindingResult br, Model m) {
        buildForm(m);
        m.addAttribute("request", rq);

        if (br.hasErrors()) {
            System.out.println("found error");
            System.out.println(br.getFieldErrors());
            return "form";
        }

        RequestResult rr = mgr.process(rq.toRequest());

        if (rr.hasError()) displayError(m, rr);
        else insertResult(m, rr);

        return "form";
    }

    @PostMapping ("/commandline")
    public String processCommandline(String command, Model m) {
        m.addAttribute("command", command);

        Request rq = Request.parse(command);
        RequestResult rr = mgr.process(rq);

        if (rr.hasError()) displayError(m, rr);
        else insertResult(m, rr);
        rr.clear();

        return "commandline";
    }

    private void insertResult(Model m, RequestResult rr) {
        m.addAttribute("requestresult", rr.copy());
    }

    private void displayError(Model m, RequestResult rr) {
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
