package AppRunner.Controllers;

import AppRunner.Datacontainers.*;
import AppRunner.Validation.RequestValidator;
import EntryHandling.Entry.ReadingStatus;
import EntryHandling.Entry.WritingStatus;
import Processing.RequestResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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

    final RequestService rs;

    public RequestController(RequestService rs) {
        this.rs = rs;
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

    @PostMapping ("/form")
    public String processForm(@Valid RequestDummy rd, BindingResult br, Model m) {
        buildForm(m);
        m.addAttribute("request", rd);

        if (br.hasErrors()) {
            System.out.println("found error");
            System.out.println(br.getFieldErrors()
                                 .stream()
                                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                 .collect(Collectors.joining("\n")));
            return "form";
        }

        RequestResult rr = rs.processForm(rd);

        if (rr.hasError()) displayError(m, rr);
        else insertResult(m, rr);

        return "form";
    }

    private void insertResult(Model m, RequestResult rr) {
        m.addAttribute("requestresult", rr.copy());
    }

    private void displayError(Model m, RequestResult rr) {
        m.addAttribute("error", rr.error());
    }

    private void buildForm(Model m) {
        m.addAttribute("operators", Operator.formoperators());
        m.addAttribute("writing_statuses", WritingStatus.selectableWS());
        m.addAttribute("reading_statuses", ReadingStatus.displayableRS());
        m.addAttribute("genres", Genre.values());
        m.addAttribute("booktypes", Booktype.selectableBooktypes());
        m.addAttribute("books", rs.books());
        m.addAttribute("secret_books", rs.secretbooks());
        m.addAttribute("displaying_options", Attribute.displayingOptions());
        m.addAttribute("change_options", ChangeForm.inputOptions());
        m.addAttribute("sorting_options", Attribute.sortingOptions());
        m.addAttribute("grouping_options", Attribute.groupingOptions());
    }
}
