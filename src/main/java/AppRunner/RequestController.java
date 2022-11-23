package AppRunner;

import AppRunner.Datastructures.Operators;
import AppRunner.Datastructures.Request;
import Management.Manager;
import Processing.RequestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RequestController {
    static RequestResult rr = new RequestResult();
    static Manager mgr = new Manager();


    static {
        mgr.setRr(rr);
        mgr.init();
        mgr.start();
    }

    @GetMapping ("/request")
    public String show(Model m) {
        m.addAttribute("command", Request.standard());
        m.addAttribute("operators", Operators.values());
        return "request";
    }

    @PostMapping ("/request")
    public String process(Request rq, Model m) {
        mgr.process(rq.command());

        rebuildForm(rq, m);

        if (rr.hasError()) displayError(m);
        else insertResult(m);
        rr.clear();

        return "request";
    }

    private void insertResult(Model m) {
        m.addAttribute("requestresult", rr.copy());
    }

    private void displayError(Model m) {
        m.addAttribute("error", rr.error());
    }

    private void rebuildForm(Request rq, Model m) {
        m.addAttribute("command", rq.command());
    }
}
