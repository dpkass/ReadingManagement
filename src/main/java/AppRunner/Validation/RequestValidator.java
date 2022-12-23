package AppRunner.Validation;

import AppRunner.Datastructures.Operator;
import AppRunner.Datastructures.RequestDummy;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;

import java.util.List;

public class RequestValidator implements org.springframework.validation.Validator {

    static Errors errors;

    @Override
    public boolean supports(Class<?> clazz) {
        if (!RequestDummy.class.isAssignableFrom(clazz)) {
            System.out.println("Wrong Validator");
            return true;
        }
        return RequestDummy.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        RequestDummy rd = (RequestDummy) target;
        RequestValidator.errors = errors;

        String opstr = rd.getOperator();
        if (!validateOperator(opstr)) return;
        switch (Operator.getOperator(opstr)) {
            case New -> validateNew(rd);
            case Read, ReadTo -> validateRead(rd);
            case Add -> validateAdd(rd);
            case Change -> validateChange(rd);
            case Open -> validateOpen(rd);
            case Show -> validateShow(rd);
            case List -> validateList(rd);
            case Recommend, ListAll, Help -> {}
        }

        addErrors();
    }

    private boolean validateOperator(String opstr) {
        return Validator.validateOperator(opstr);
    }

    private void validateNew(RequestDummy rd) {
        List<String> param = List.of(rd.getBooknew(), rd.getNewpagevalue(), rd.getNewlinkvalue(), rd.getNewwsvalue(), rd.getNewlrvalue());
        Validator.validateNew(param);
    }

    private void validateRead(RequestDummy rd) {
        List<String> param = List.of(rd.getBooksel(), rd.getReadvalue());
        Validator.validateRead(param);
    }

    private void validateAdd(RequestDummy rd) {
        List<String> param = List.of(rd.getBooksel(), rd.getAddvalue());
        Validator.validateAdd(param);
    }

    private void validateChange(RequestDummy rd) {
        List<String> param = List.of(rd.getChangeattribute(), rd.getBooksel(), rd.getChangevalue());
        Validator.validateChange(param);
    }

    private void validateOpen(RequestDummy rd) {
        Validator.validateBooksel(rd.getBooksel());
    }

    private void validateShow(RequestDummy rd) {
        Validator.validateBooksel(rd.getBooksel());
    }

    private void validateList(RequestDummy rd) {
        List<String> param = List.of(rd.getSortby(), rd.getGroupby());
        Validator.validateList(param);
    }

    // setter/getter
    private void addErrors() {
        Validator.errors().forEach(e -> RequestValidator.errors.rejectValue(e[0], e[1], e[2]));
        Validator.errors().clear();
    }
}
