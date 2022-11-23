package AppRunner;

import AppRunner.Datastructures.Operators;
import IOHandling.SavingIOHandler;
import Management.Manager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RequestController {
    static SavingIOHandler io = new SavingIOHandler();
    static Manager mgr = new Manager();


    static {
        mgr.setIo(io);
        mgr.init();
        mgr.start();
    }

    @GetMapping ("/request")
    public String show(Model m) {
        m.addAttribute("command", "sth");
        m.addAttribute("operators", Operators.values());
        return "request";
    }

    @PostMapping ("/request")
    public String process(String command, Model m) {
        mgr.process(command);

        createModel(command, m);
        io.clear();
        return "request";
    }

    private void createModel(String command, Model m) {
        m.addAttribute("command", command);
        m.addAttribute("out", List.copyOf(io.out()));
    }


}
