package AppRunner.Datastructures;

import AppRunner.Validation.CommandValidator;
import Management.Helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//rework validate
public class RequestBuilder {
    // main
    private static String operator;
    private static String helpoperator;
    private static String booksel;
    private static String booknew;

    // addtional change
    private static String changeattribute;
    private static String changevalue;
    // addtional add
    private static String addvalue;
    // addtional new
    private static float newpagevalue;
    private static String newlinkvalue;
    private static String newwsvalue;
    private static String newlrvalue;
    // additional read
    private static float readvalue;
    // additional wait
    private static String waituntil;

    // list display
    private static DisplayAttributesForm daf;
    private static List<String[]> filters;

    // order args
    private static String sortby;
    private static String groupby;

    public static Request build(String command) {

        try {
            List<String> parts = Pattern.compile("\"([^\"]*)\"|(\\S+)")
                                        .matcher(command)
                                        .results()
                                        .map(str -> str.group(1) != null ? str.group(1) : str.group(2))
                                        .toList();

            operator = parts.get(0);
            CommandValidator.validateOperator(operator);

            Operator op = Operator.getOperator(parts.get(0));

            parts = parts.size() > 1 ? parts.subList(1, parts.size()) : List.of();
            switch (op) {
                case New -> buildNew(parts);
                case Read, ReadTo -> buildRead(parts);
                case Add -> buildAdd(parts);
                case Change -> buildChange(parts);
                case Open -> buildOpen(parts);
                case Show -> buildShow(parts);
                case List -> buildList(parts);
                case Wait -> buildWait(parts);
                case Pause -> buildPause(parts);
                case Help -> buildHelp(parts);
                case ListAll, Recommend -> {}
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RequestParsingException(Collections.singletonList(new String[] { "command", "missingargs", "Too few arguments provided. Use help for more Info." }), command);
        }

        return toRequest();
    }

    private static void buildNew(List<String> parts) {
        CommandValidator.validateNew(parts);
        try {
            booknew = parts.get(0);
            newpagevalue = Float.parseFloat(parts.get(1));
            newlinkvalue = parts.get(2);
            newwsvalue = parts.get(3);
            newlrvalue = parts.get(4);
        } catch (IndexOutOfBoundsException ignored) {}
    }

    private static void buildRead(List<String> parts) {
        CommandValidator.validateRead(parts);
        booksel = parts.get(0);
        readvalue = Float.parseFloat(parts.get(1));
    }

    private static void buildAdd(List<String> parts) {
        CommandValidator.validateAdd(parts);
        booksel = parts.get(0);
        addvalue = parts.get(1);
    }

    private static void buildChange(List<String> parts) {
        CommandValidator.validateChange(parts);
        changeattribute = parts.get(0);
        booksel = parts.get(1);
        changevalue = parts.get(2);
    }

    private static void buildOpen(List<String> parts) {
        CommandValidator.validateOpen(parts.get(0));
        booksel = parts.get(0);
    }

    // rework validate DAF (try catch)
    private static void buildShow(List<String> parts) {
        CommandValidator.validateShow(parts.get(0));
        booksel = parts.get(0);
        daf = DisplayAttributesUtil.build(parts.size() > 1 ? parts.subList(1, parts.size()) : List.of());
    }

    // rework validate
    private static void buildList(List<String> parts) {
        Map<Boolean, List<String>> type0orderfiltermap = parts.stream().collect(Collectors.partitioningBy(s -> s.matches(".*[=<>].*")));
        daf = DisplayAttributesUtil.build(type0orderfiltermap.get(false));

        filters = type0orderfiltermap.get(true)
                                     .stream()
                                     .map(s -> s.split("((?<=[=<>])|(?=[=<>]))"))
                                     .filter(RequestBuilder::setOrder)
                                     .collect(Collectors.toList());
    }

    private static boolean setOrder(String[] t) {
        switch (Helper.representation(t[0])) {
            case "sb" -> sortby = t[2];
            case "gb" -> groupby = t[2];
            default -> {return true;}
        }
        return false;
    }

    private static void buildWait(List<String> parts) {
        System.out.println(parts);
        CommandValidator.validateWait(parts);
        booksel = parts.get(0);
        waituntil = parts.size() == 1 ? null : parts.get(1);
    }

    private static void buildPause(List<String> parts) {
        CommandValidator.validatePause(parts.get(0));
        booksel = parts.get(0);
    }

    private static void buildHelp(List<String> parts) {
        if (parts != null) helpoperator = parts.get(0);
    }

    private static Request toRequest() {
        Request request = new Request();

        LocalDateTime lr = newlrvalue == null ? null : LocalDateTime.parse(newlrvalue);
        LocalDate wu = newlrvalue == null ? null : LocalDate.parse(waituntil);
        List<Filter<?>> filters = RequestBuilder.filters == null ? null : RequestBuilder.filters.stream()
                                                                                                .map(Filter::createFilter)
                                                                                                .collect(Collectors.toList());

        request.setOperator(operator);
        request.setHelpoperator(helpoperator);
        request.setBooknew(booknew);
        request.setBooksel(booksel);
        request.setChangeattribute(changeattribute);
        request.setChangevalue(changevalue);
        request.setAddvalue(addvalue);
        request.setNewpagevalue(newpagevalue);
        request.setNewlinkvalue(newlinkvalue);
        request.setNewwsvalue(newwsvalue);
        request.setNewlrvalue(lr);
        request.setReadvalue(readvalue);
        request.setWaituntil(wu);
        request.setDaf(daf);
        request.setFilters(filters);
        request.setSortby(sortby);
        request.setGroupby(groupby);

        return request;
    }
}
