package AppRunner.Validation;

import AppRunner.Datastructures.Attribute;
import AppRunner.Datastructures.Operator;
import EntryHandling.Entry.ReadingStatus;
import EntryHandling.Entry.WritingStatus;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static List<String[]> errors = new ArrayList<>();

    static boolean validateOperator(String opstr) {
        if (opstr == null || opstr.isBlank()) {
            addError("operator", "operatornotchosen", "Operator must be chosen");
            return false;
        }
        if (Operator.getOperator(opstr) == null) {
            addError("operator", "operatornotoperator", "Operator is not valid");
            return false;
        }
        return true;
    }

    public static void validateNew(List<String> parts) {
        String booknew = parts.get(0);
        if (booknew == null || booknew.isBlank()) addError("booknew", "booknewblank", "Bookname must not be blank");
        if (parts.size() < 2) return;

        String newpagevalue = parts.get(1);
        if (newpagevalue != null && !newpagevalue.isBlank() && !Validator.isNumber(newpagevalue))
            addError("newpagevalue", "newpagenotnumber", "Page/Chapter must be number");
        if (parts.size() < 3) return;

        String newlinkvalue = parts.get(2);
        if (newlinkvalue != null && !newlinkvalue.isBlank() && !Validator.isLink(newlinkvalue))
            addError("newlinkvalue", "newlinknotlink", "Link is not valid");
        if (parts.size() < 4) return;

        if (WritingStatus.getStatus(parts.get(3)) == null) addError("newwsvalue", "newwsnotws", "Link is not valid");
        if (parts.size() < 5) return;

        validateLr(parts.get(4));
    }

    static void validateRead(List<String> parts) {
        validateBooksel(parts.get(0));
        validateReadval(parts.get(1));
    }

    public static void validateAdd(List<String> parts) {
        String addvalue = parts.get(1);
        validateBooksel(parts.get(0));
        if (addvalue == null || addvalue.isBlank()) addError("addvalue", "addnull", "Abbreviation to add must not be blank");
    }

    static void validateChange(List<String> parts) {
        validateBooksel(parts.get(1));
        Attribute chatt = Attribute.getAttribute(parts.get(0));
        if (chatt == null) {
            addError("changeattribute", "changeattributenotattribute", "Type to change must be an attribute");
            return;
        }
        if (!Attribute.changingOptions().contains(chatt)) {
            addError("changeattribute", "changeattributenotattribute", "Type to change must be a changeable attribute");
            return;
        }
        String chval = parts.get(2);
        if (chval == null || chval.isBlank()) {
            addError("changevalue", "changevalueblank", "Change value must not be blank");
            return;
        }
        switch (chatt) {
            case name -> {}
            case link -> {
                if (!Validator.isLink(chval))
                    addError("changevalue", "changevaluenotlink", "Change value must be a changeable attribute");
            }
            case rating -> {
                if (!Validator.isNumber(chval))
                    addError("changevalue", "changevaluenotnumber", "Change value must be a number for rating changes");
            }
            case writingStatus -> {
                if (WritingStatus.getStatus(chval) == WritingStatus.Default)
                    addError("changevalue", "changevaluenotws", "Change value must be a writing status");
            }
            case readingStatus -> {
                if (ReadingStatus.getStatus(chval) == ReadingStatus.Default)
                    addError("changevalue", "changevaluenotrs", "Change value must be a reading status");
            }
        }
    }

    static void validateList(List<String> parts) {
        validateSort(parts.get(0));
        validateGroup(parts.get(1));
    }

    public static void validateWait(String booksel, String waituntil) {
        validateBooksel(booksel);
        validateWu(waituntil);
    }

    // single field validators
    static void validateLr(String newlrvalue) {
        if (newlrvalue == null || newlrvalue.isBlank()) return;

        LocalDateTime ldt = Validator.toLDT(newlrvalue);
        if (ldt == null) addError("newlrvalue", "newlrnotdate", "Lastread is not a date");
        else if (ldt.isAfter(LocalDateTime.now())) addError("newlrvalue", "newlrnotpast", "Lastread must be in the past");
    }

    private static void validateWu(String waituntil) {
        if (waituntil == null || waituntil.isBlank()) return;

        LocalDate ld = Validator.toLD(waituntil);
        if (ld == null) addError("waituntil", "waitwunotdate", "Wait-Until is not a date");
        else if (ld.isBefore(LocalDate.now())) addError("waituntil", "waitwunotfuture", "Wait-Until must be in the future");
    }

    private static void validateReadval(String readvalue) {
        if (!Validator.isNumber(readvalue)) addError("readvalue", "readnotnumber", "Readvalue must be a number");
        if (Float.parseFloat(readvalue) < 0) addError("readvalue", "readnegative", "Readvalue must not be negative");
    }

    static void validateSort(String sortby) {
        if (sortby == null || sortby.isBlank()) return;

        Attribute soatt = Attribute.getAttribute(sortby);
        if (soatt == null) {
            addError("sortby", "sortnotattribute", "Type to sort by must be an attribute");
            return;
        }
        if (!Attribute.sortingOptions().contains(soatt))
            addError("sortby", "sortnotsortingattribute", "Type to sort by must be a sortable attribute");
    }

    static void validateGroup(String groupby) {
        if (groupby == null || groupby.isBlank()) return;

        Attribute gratt = Attribute.getAttribute(groupby);
        if (gratt == null) {
            addError("groupby", "sortnotattribute", "Type to group by must be an attribute");
            return;
        }
        if (!Attribute.groupingOptions().contains(gratt))
            addError("groupby", "sortnotsortingattribute", "Type to group by must be a group-able attribute");
    }

    static void validateBooksel(String booksel) {
        if (booksel == null || booksel.isBlank()) addError("booksel", "bookselblank", "A book must be selected");
    }


    // typecheckers
    static boolean isLink(String newlinkvalue) {
        try {
            URI.create(newlinkvalue);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    static boolean isNumber(String chval) {
        try {
            Float.parseFloat(chval);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    static LocalDateTime toLDT(String newlrvalue) {
        try {
            return LocalDateTime.parse(newlrvalue);
        } catch (DateTimeParseException dpe) {
            return null;
        }
    }

    static LocalDate toLD(String newlrvalue) {
        try {
            return LocalDate.parse(newlrvalue);
        } catch (DateTimeParseException dpe) {
            return null;
        }
    }


    // setter/getter
    public static List<String[]> errors() {
        return errors;
    }

    private static void addError(String field, String code, String defaultmessage) {
        errors.add(new String[] { field, code, defaultmessage });
    }
}
