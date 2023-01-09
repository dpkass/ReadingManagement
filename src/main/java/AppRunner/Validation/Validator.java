package AppRunner.Validation;

import AppRunner.Datacontainers.*;
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

    public static void validateNew(List<String> parts, List<String> genres) {
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
        if (parts.size() < 6) return;

        validateBooktype(parts.get(5));
        validateGenres(genres);
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

    static void validateChange(String booksel, List<ChangeValueWrapper> values) {
        validateBooksel(booksel);
//        for (Object value : values) {
//            String chval = (String) value;
//            switch ((Attribute) value) {
//                case Name -> {
//                    if (checkChangeValueNullOrBlank(chval, "changetextvalue")) {}
//                }
//                case Link -> {
//                    if (checkChangeValueNullOrBlank(chval, "changetextvalue")) return;
//                    if (!isLink(chval)) addError("changetextvalue", "changevaluenotlink", "Change value must be a changeable attribute");
//                }
//                case StoryRating, CharactersRating, DrawingRating, Rating -> {
//                    if (checkChangeValueNullOrBlank(chval, "changenumbervalue")) return;
//                    if (!isNumber(chval)) {
//                        addError("changenumbervalue", "changevaluenotnumber", "Change value must be a number for rating changes");
//                        return;
//                    }
//                    if (!isRating(chval))
//                        addError("changenumbervalue", "changevaluenotrating", "Change value must be a valid rating (between 0 and 5)");
//                }
//                case WritingStatus -> {
//                    if (checkChangeValueNullOrBlank(chval, "changewsvalue")) return;
//                    if (WritingStatus.getStatus(chval) == WritingStatus.Default)
//                        addError("changewsvalue", "changevaluenotws", "Change value must be a writing status");
//                }
//                case Booktype -> {
//                    if (checkChangeValueNullOrBlank(chval, "changebtvalue")) return;
//                    if (Booktype.getBooktype(chval) == null)
//                        addError("changebtvalue", "changevaluenotbt", "Change value must be a booktype");
//                }
//            }
//        }
    }

    static void validateList(RequestDummy rd) {
        validateSort(rd.getSortby());
        validateGroup(rd.getGroupby());
        validateFilters(rd);
    }

    static void validateFilters(RequestDummy rd) {
        // chapter filter
        if (rd.getFilterchapterop().isBlank() && rd.getFilterchapter() != 0.0)
            addError("filterchapterop", "filterchapteropblank", "An operator for chapter-filter must be selected");
        // rating filter
        if (rd.getFilterratingop().isBlank() && rd.getFilterrating() != 0.0)
            addError("filterratingop", "filterratingopblank", "An operator for rating-filter must be selected");
        // lr filter
        if (rd.getFilterlrop().isBlank() && !rd.getFilterlr().isBlank())
            addError("filterlrop", "filterlropblank", "An operator for last-read-filter must be selected");
        else if (!rd.getFilterlrop().isBlank() && rd.getFilterlr().isBlank())
            addError("filterlr", "filterlrblank", "The value for last-read-filter must be set, if operator is selected");
        if (!rd.getFilterlr().isBlank() && toLD(rd.getFilterlr()) == null)
            addError("filterlr", "filterlrnotdate", "The value for last-read-filter must be a date");
        // wu filter
        if (rd.getFilterwuop().isBlank() && !rd.getFilterwu().isBlank())
            addError("filterwuop", "filterwuopblank", "An operator for wait-until-filter must be selected");
        else if (!rd.getFilterwuop().isBlank() && rd.getFilterwu().isBlank())
            addError("filterwu", "filterwublank", "The value for wait-until-filter must be set, if operator is selected");
        if (!rd.getFilterwu().isBlank() && toLD(rd.getFilterwu()) == null)
            addError("filterwu", "filterwunotdate", "The value for wait-until-filter must be a date");

        validateRSFilters(rd.getFilterrs());
        validateWSFilters(rd.getFilterws());
    }

    public static void validateWait(List<String> parts) {
        String booksel = parts.size() == 0 ? null : parts.get(0);
        String waituntil = parts.size() == 1 ? null : parts.get(1);
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

    private static void validateRSFilters(List<String> filters) {
        filters.stream()
               .filter(filter -> ReadingStatus.getStatus(filter) == ReadingStatus.Default)
               .forEach(filter -> addError("filterrs", "filterrsnotrs", "The values for Reading-Status-filter must be a valid reading-status {%s}".formatted(filter)));
    }

    private static void validateWSFilters(List<String> filters) {
        filters.stream()
               .filter(filter -> WritingStatus.getStatus(filter) == WritingStatus.Default)
               .forEach(filter -> addError("filterws", "filterwsnotws", "The values for Writing-Status-filter must be a valid writing-status {%s}".formatted(filter)));
    }

    private static void validateBooktype(String s) {
        Booktype bt = Booktype.getBooktype(s);
        if (bt == null) addError("newbooktypevalue", "booktypenotbooktype", "The booktype must be a valid booktype");
    }

    private static void validateGenres(List<String> genres) {
        for (String genre : genres) {
            Genre g = Genre.getGenre(genre);
            if (g == null) addError("newgenresvalue", "genrenotgenre", "The genre " + genre + " must be a valid genre");
        }
    }

    private static boolean checkChangeValueNullOrBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            addError(field, "changevalueblank", "Change value must not be blank");
            return true;
        }
        return false;
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

    private static boolean isRating(String chval) {
        float f = Float.parseFloat(chval);
        return f >= 0 && f <= 5;
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
