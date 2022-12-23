package AppRunner;

import AppRunner.Datastructures.RequestParsingException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RequestControllerAdvice {

    @ExceptionHandler (RequestParsingException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public String handleCommandlineParsingException(RequestParsingException rpe, Model m) {
        rpe.errors().forEach(e -> m.addAttribute(e[0] + "error", e[2]));
        if (rpe.command() != null) m.addAttribute(rpe.command());
        System.out.println(m);
        return "commandline";
    }
}
