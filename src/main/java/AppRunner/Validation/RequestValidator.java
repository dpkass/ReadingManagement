package AppRunner.Validation;

import AppRunner.Datastructures.Attribute;
import AppRunner.Datastructures.Operator;
import AppRunner.Datastructures.RequestDummy;
import EntryHandling.Entry.ReadingStatus;
import EntryHandling.Entry.WritingStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class RequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
//        should be deleted, but doesnt work else
        if (!RequestDummy.class.equals(clazz)) return true;
        return RequestDummy.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        RequestDummy rd = (RequestDummy) target;

        String opstr = rd.getOperator();
        if (!validateOperator(opstr, errors)) return;
        switch (Operator.getOperator(opstr)) {
            case New -> validateNew(rd, errors);
            case Read, ReadTo -> validateRead(rd, errors);
            case Add -> validateAdd(rd, errors);
            case Change -> validateChange(rd, errors);
            case Open -> validateOpen(rd, errors);
            case Show -> validateShow(rd, errors);
            case List -> validateList(rd, errors);
            case Recommend, ListAll, Help -> {}
        }
    }

    private boolean validateOperator(String opstr, Errors errors) {
        if (opstr == null || opstr.isBlank()) {
            errors.rejectValue("operator", "operatornotchosen", "Operator must be chosen");
            return false;
        }
        if (Operator.getOperator(opstr) == null) {
            errors.rejectValue("operator", "operatornotoperator", "Operator is not valid");
            return false;
        }
        return true;
    }

    private void validateList(RequestDummy rd, Errors errors) {
        validateSort(rd.getSortby(), errors);
        validateGroup(rd.getGroupby(), errors);
    }

    private void validateShow(RequestDummy rd, Errors errors) {
        validateBooksel(rd.getBooksel(), errors);
    }

    private void validateOpen(RequestDummy rd, Errors errors) {
        validateBooksel(rd.getBooksel(), errors);
    }

    private void validateChange(RequestDummy rd, Errors errors) {
        Attribute chatt = Attribute.getAttribute(rd.getChangeattribute());
        if (chatt == null) {
            errors.rejectValue("changeattribute", "changeattributenotattribute", "Type to change must be an attribute");
            return;
        }
        if (!Attribute.changingOptions().contains(chatt)) {
            errors.rejectValue("changeattribute", "changeattributenotattribute", "Type to change must be a changeable attribute");
            return;
        }
        String chval = rd.getChangevalue();
        if (chval == null || chval.isBlank()) {
            errors.rejectValue("changevalue", "changevalueblank", "Change value must not be blank");
            return;
        }
        switch (chatt) {
            case name -> {}
            case link -> {
                if (!isLink(chval)) errors.rejectValue("changevalue", "changevaluenotlink", "Change value must be a changeable attribute");
            }
            case rating -> {
                if (!isNumber(chval))
                    errors.rejectValue("changevalue", "changevaluenotnumber", "Change value must be a number for rating changes");
            }
            case writingStatus -> {
                if (WritingStatus.getStatus(chval) == WritingStatus.Default)
                    errors.rejectValue("changevalue", "changevaluenotws", "Change value must be a writing status");
            }
            case readingStatus -> {
                if (ReadingStatus.getStatus(chval) == ReadingStatus.Default)
                    errors.rejectValue("changevalue", "changevaluenotrs", "Change value must be a reading status");
            }
        }
    }

    private void validateAdd(RequestDummy rd, @NotNull Errors errors) {
        validateBooksel(rd.getBooksel(), errors);
        validateAddvalue(rd.getAddvalue(), errors);
    }

    private void validateRead(RequestDummy rd, @NotNull Errors errors) {
        validateBooksel(rd.getBooksel(), errors);
        if (rd.getReadvalue() < 0) errors.rejectValue("readvalue", "readnegative", "Readvalue must not be negative");
    }

    private void validateNew(RequestDummy rd, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "booknew", "booknewblank", "Bookname must not be blank");
        if (rd.getNewlinkvalue() != null && !isLink(rd.getNewlinkvalue()))
            errors.rejectValue("newlinkvalue", "newlinknotlink", "Link is not valid");
        if (WritingStatus.getStatus(rd.getNewwsvalue()) == null) errors.rejectValue("newwsvalue", "newwsnotws", "Link is not valid");
        validateLr(rd.getNewlrvalue(), errors);
    }

    // single field validators

    private void validateLr(String newlrvalue, Errors errors) {
        if (newlrvalue == null || newlrvalue.isBlank()) return;

        LocalDateTime ldt = toLDT(newlrvalue);
        if (ldt == null) {
            errors.rejectValue("newlrvalue", "newlrnotdate", "Lastread is not a date");
            return;
        }
        if (ldt.isAfter(LocalDateTime.now())) errors.rejectValue("newlrvalue", "newlrnotpast", "Lastread must be in the past");
    }

    private void validateAddvalue(String addvalue, Errors errors) {
        if (addvalue == null || addvalue.isBlank()) errors.rejectValue("addvalue", "addnull", "Abbreviation to add must not be blank");
        // rework to look up if abb is in db
    }

    private void validateSort(String sortby, Errors errors) {
        if (sortby == null || sortby.isBlank()) return;

        Attribute soatt = Attribute.getAttribute(sortby);
        if (soatt == null) {
            errors.rejectValue("sortby", "sortnotattribute", "Type to sort by must be an attribute");
            return;
        }
        if (!Attribute.sortingOptions().contains(soatt))
            errors.rejectValue("sortby", "sortnotsortingattribute", "Type to sort by must be a sortable attribute");
    }

    private void validateGroup(String groupby, Errors errors) {
        if (groupby == null || groupby.isBlank()) return;

        Attribute gratt = Attribute.getAttribute(groupby);
        if (gratt == null) {
            errors.rejectValue("groupby", "sortnotattribute", "Type to group by must be an attribute");
            return;
        }
        if (!Attribute.groupingOptions().contains(gratt))
            errors.rejectValue("groupby", "sortnotsortingattribute", "Type to group by must be a group-able attribute");
    }

    // rework to look up if book is in db
    private void validateBooksel(String booksel, Errors errors) {}


    // typecheckers
    private boolean isLink(String newlinkvalue) {
        try {
            URI.create(newlinkvalue);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    private boolean isNumber(String chval) {
        try {
            Float.parseFloat(chval);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private LocalDateTime toLDT(String newlrvalue) {
        try {
            return LocalDateTime.parse(newlrvalue);
        } catch (DateTimeParseException | IllegalArgumentException dpe) {
            return null;
        }
    }

}
