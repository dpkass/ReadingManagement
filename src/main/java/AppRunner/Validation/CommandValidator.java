package AppRunner.Validation;

import AppRunner.Datacontainers.RequestParsingException;

import java.util.ArrayList;
import java.util.List;

public class CommandValidator {

    public static List<String[]> errors = new ArrayList<>();

    public static void validateOperator(String opstr) {
        Validator.validateOperator(opstr);
        end();
    }

    public static void validateNew(List<String> param) {
        Validator.validateNew(param);
        end();
    }

    public static void validateRead(List<String> param) {
        Validator.validateRead(param);
        end();
    }

    public static void validateAdd(List<String> param) {
        Validator.validateAdd(param);
        end();
    }

    public static void validateChange(List<String> param) {
        Validator.validateChange(param);
        end();
    }

    public static void validateOpen(String param) {
        Validator.validateBooksel(param);
        end();
    }

    public static void validateList(List<String> param) {
//        Validator.validateList(param);
        end();
    }

    public static void validateWait(List<String> param) {
        Validator.validateWait(param);
        end();
    }

    public static void validatePause(String param) {
        Validator.validateBooksel(param);
        end();
    }

    private static void end() {
        errors.addAll(Validator.errors());
        Validator.errors().clear();
        if (!errors.isEmpty()) throw new RequestParsingException(errors());
    }

    // setter/getter
    public static List<String[]> errors() {
        return errors;
    }
}
