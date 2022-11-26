package AppRunner.Datastructures;

import Management.Helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//rework validate
public class RequestBuilder {

    // main
    private static Operator operator;
    private static Operator helpoperator;
    private static String book;

    // addtional change
    private static String changeattribute;
    private static String changevalue;
    // addtional add
    private static String addvalue;
    // addtional new
    private static float newpagevalue;
    private static String newlinkvalue;
    private static String newwsvalue;
    private static LocalDateTime newlrvalue;
    // additional read
    private static float readvalue;

    // list display
    private static DisplayAttributesForm daf;
    private static List<Filter<?>> filters;

    // order args
    private static String sortby;
    private static String groupby;

    public static Request build(String command) {
        List<String> parts = Pattern.compile("([^\"]\\S*|\".*\")\\s*").matcher(command).results().map(str -> str.group(1)).toList();

        operator = Operator.getOperator(parts.get(0));
        parts = parts.size() > 1 ? parts.subList(1, parts.size() - 1) : List.of();
        switch (operator) {
            case New -> buildNew(parts);
            case Read, ReadTo -> buildRead(parts);
            case Add -> buildAdd(parts);
            case Change -> buildChange(parts);
            case Open -> buildOpen(parts);
            case Show -> buildShow(parts);
            case List -> buildList(parts);
            case Help -> buildHelp(parts);
            case ListAll, Recommend -> {}
        }

        return toRequest();
    }

    private static void buildNew(List<String> parts) {
        book = parts.get(0);
        newpagevalue = Float.parseFloat(parts.get(1));
        newlinkvalue = parts.get(2);
        newwsvalue = parts.get(3);
        newlrvalue = LocalDateTime.parse(parts.get(4), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    private static void buildRead(List<String> parts) {
        book = parts.get(0);
        readvalue = Float.parseFloat(parts.get(1));
    }

    private static void buildAdd(List<String> parts) {
        book = parts.get(0);
        addvalue = parts.get(1);
    }

    private static void buildChange(List<String> parts) {
        changeattribute = parts.get(0);
        changevalue = parts.get(1);
    }

    private static void buildOpen(List<String> parts) {
        book = parts.get(0);
    }

    private static void buildShow(List<String> parts) {
        book = parts.get(0);
        daf = DisplayAttributesUtil.build(parts.size() > 1 ? parts.subList(1, parts.size() - 1) : List.of());
    }

    private static void buildList(List<String> parts) {
        Map<Boolean, List<String>> type0orderfiltermap = parts.stream().collect(Collectors.partitioningBy(s -> s.matches(".*[=<>].*")));
        daf = DisplayAttributesUtil.build(type0orderfiltermap.get(false));

        filters = type0orderfiltermap.get(true)
                                     .stream()
                                     .map(s -> s.split("((?<=[=<>])|(?=[=<>]))"))
                                     .filter(RequestBuilder::setOrder)
                                     .map(Filter::createFilter)
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

    private static void buildHelp(List<String> parts) {
        if (parts != null) helpoperator = Operator.getOperator(parts.get(0));
    }

    private static Request toRequest() {
        Request request = new Request();

        request.setOperator(operator);
        request.setHelpoperator(helpoperator);
        request.setBook(book);
        request.setChangeattribute(changeattribute);
        request.setChangevalue(changevalue);
        request.setAddvalue(addvalue);
        request.setNewpagevalue(newpagevalue);
        request.setNewlinkvalue(newlinkvalue);
        request.setNewwsvalue(newwsvalue);
        request.setNewlrvalue(newlrvalue);
        request.setReadvalue(readvalue);
        request.setDaf(daf);
        request.setFilters(filters);
        request.setSortby(sortby);
        request.setGroupby(groupby);

        return request;
    }
}
