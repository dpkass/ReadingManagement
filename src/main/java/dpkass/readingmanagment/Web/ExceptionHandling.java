package dpkass.readingmanagment.Web;

import dpkass.readingmanagment.Domain.Exceptions.FileNotValidException;
import dpkass.readingmanagment.Domain.Exceptions.RequestParsingException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler (RequestParsingException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public String handleCommandlineParsingException(RequestParsingException rpe, Model m) {
        rpe.errors().forEach(e -> m.addAttribute(e[0] + "error", e[2]));
        if (rpe.command() != null) m.addAttribute(rpe.command());
        return "commandline";
    }

    @ExceptionHandler (FileNotValidException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public String handleFileNotValidException(FileNotValidException fnve, Model m) {
        m.addAttribute(fnve.secret() ? "secretfile" : "file", fnve.filename());
        return "index";
    }
}
