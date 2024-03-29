package dpkass.readingmanagment.Web.Validation;

import dpkass.readingmanagment.Domain.Aggregates.RequestParts.ChangeValueWrapper;
import dpkass.readingmanagment.Domain.Types.Operator;
import dpkass.readingmanagment.WebService.DataContainers.RequestDummy;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;

import java.util.List;

public class RequestValidator implements org.springframework.validation.Validator {

    static Errors errors;
    private RequestDummy rd;

    @Override
    public boolean supports(Class<?> clazz) {
        if (!RequestDummy.class.isAssignableFrom(clazz)) {
            System.err.println("Wrong Validator: " + clazz);
            return true;
        }
        return RequestDummy.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        rd = (RequestDummy) target;
        RequestValidator.errors = errors;

        String opstr = rd.getOperator();
        if (validateOperator(opstr)) switch (Operator.getOperator(opstr)) {
            case New -> validateNew();
            case Read, ReadTo -> validateRead();
            case Add -> validateAdd();
            case Change -> validateChange();
            case Open -> validateOpen();
            case List -> validateList();
            case Wait -> validateWait();
            case Pause -> validatePause();
            case Recommend, Help -> {}
        }

        addErrors();
    }

    private boolean validateOperator(String opstr) {
        return Validator.validateOperator(opstr);
    }

    private void validateNew() {
        List<String> param = List.of(rd.getBooknew(), rd.getNewpagevalue(), rd.getNewlinkvalue(), rd.getNewwsvalue(), rd.getNewlrvalue(), rd.getNewbooktypevalue());
        Validator.validateNew(param, rd.getNewgenresvalue());
    }

    private void validateRead() {
        List<String> param = List.of(rd.getBooksel(), rd.getReadvalue());
        Validator.validateRead(param);
    }

    private void validateAdd() {
        List<String> param = List.of(rd.getBooksel(), rd.getAddvalue());
        Validator.validateAdd(param);
    }

    private void validateChange() {
        List<ChangeValueWrapper> values = rd.getChangevalues();
        Validator.validateChange(rd.getBooksel(), values);
    }

    private void validateOpen() {
        Validator.validateBooksel(rd.getBooksel());
    }

    private void validateList() {
        Validator.validateList(rd);
    }

    private void validateWait() {
        List<String> param = List.of(rd.getBooksel(), rd.getWaituntil());
        Validator.validateWait(param);
    }

    private void validatePause() {
        Validator.validateBooksel(rd.getBooksel());
    }

    // setter/getter
    private void addErrors() {
        Validator.errors().forEach(e -> RequestValidator.errors.rejectValue(e[0], e[1], e[2]));
        Validator.errors().clear();
    }
}
