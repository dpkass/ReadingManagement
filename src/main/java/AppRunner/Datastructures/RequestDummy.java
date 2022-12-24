package AppRunner.Datastructures;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public final class RequestDummy {
    private String operator = "";
    private String helpoperator = "";
    private String booknew;
    private String booksel = "";

    // addtional change
    private String changeattribute = "";
    private String changevalue = "";
    // addtional add
    private String addvalue = "";
    // addtional new
    private String newpagevalue = "0";
    private String newlinkvalue = "";
    private String newwsvalue = "";
    @DateTimeFormat (pattern = "dd.MM.yyyy HH:mm")
    private String newlrvalue;
    // additional read
    private String readvalue = "1";

    // list display
    private boolean displayread = false;
    private boolean displaylink = false;
    private boolean displayrating = false;
    private boolean displaylastread = false;
    private boolean displaywaituntil = false;
    private boolean displayreadingstatus = false;
    private boolean displaywritingstatus = false;
    private List<String[]> filters;

    // order args
    private String sortby;
    private String groupby;
    private boolean sortdescending;
    private boolean groupdescending;

    public Request toRequest() {
        Request request = new Request();

        List<Boolean> daf = java.util.List.of(displayread, displaylink, displayrating, displaylastread, displaywaituntil, displayreadingstatus, displaywritingstatus);
        List<Filter<?>> filters = this.filters == null ? null : this.filters.stream()
                                                                            .map(Filter::createFilter)
                                                                            .collect(Collectors.toList());
        LocalDateTime parsed = toLD();
        float newpageval = Float.parseFloat(newpagevalue);
        float readval = Float.parseFloat(readvalue);

        request.setOperator(operator);
        request.setHelpoperator(helpoperator);
        request.setBooknew(booknew);
        request.setBooksel(booksel);
        request.setChangeattribute(changeattribute);
        request.setChangevalue(changevalue);
        request.setAddvalue(addvalue);
        request.setNewpagevalue(newpageval);
        request.setNewlinkvalue(newlinkvalue);
        request.setNewwsvalue(newwsvalue);
        request.setNewlrvalue(parsed);
        request.setReadvalue(readval);
        request.setDaf(new DisplayAttributesForm(daf));
        request.setFilters(filters);
        request.setSortby(sortby);
        request.setGroupby(groupby);
        request.setSortdescending(sortdescending);
        request.setGroupdescending(groupdescending);

        return request;
    }

    private LocalDateTime toLD() {
        try {
            return LocalDateTime.parse(newlrvalue);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    public static RequestDummy standard() {
        return new RequestDummy();
    }

    @Override
    public String toString() {
        return "RequestDummy{" + "operator='" + operator + '\'' + ", helpoperator='" + helpoperator + '\'' + ", booknew='" + booknew + '\'' + ", booksel='" + booksel + '\'' + ", changeattribute='" + changeattribute + '\'' + ", changevalue='" + changevalue + '\'' + ", addvalue='" + addvalue + '\'' + ", newpagevalue=" + newpagevalue + ", newlinkvalue='" + newlinkvalue + '\'' + ", newwsvalue='" + newwsvalue + '\'' + ", newlrvalue='" + newlrvalue + '\'' + ", readvalue=" + readvalue + ", displayread=" + displayread + ", displaylink=" + displaylink + ", displayrating=" + displayrating + ", displaylastread=" + displaylastread + ", displaywaituntil=" + displaywaituntil + ", displayreadingstatus=" + displayreadingstatus + ", displaywritingstatus=" + displaywritingstatus + ", filters=" + filters + ", sortby='" + sortby + '\'' + ", groupby='" + groupby + '\'' + '}';
    }

    public void setDaf(List<Boolean> daf) {
        displayread = daf.get(0);
        displaylink = daf.get(1);
        displayrating = daf.get(2);
        displaylastread = daf.get(3);
        displaywaituntil = daf.get(4);
        displayreadingstatus = daf.get(5);
        displaywritingstatus = daf.get(6);
    }

    // getters and setters

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getHelpoperator() {
        return helpoperator;
    }

    public void setHelpoperator(String helpoperator) {
        this.helpoperator = helpoperator;
    }

    public String getBooknew() {
        return booknew;
    }

    public void setBooknew(String booknew) {
        this.booknew = booknew;
    }

    public String getBooksel() {
        return booksel;
    }

    public void setBooksel(String booksel) {
        this.booksel = booksel;
    }

    public String getChangeattribute() {
        return changeattribute;
    }

    public void setChangeattribute(String changeattribute) {
        this.changeattribute = changeattribute;
    }

    public String getChangevalue() {
        return changevalue;
    }

    public void setChangevalue(String changevalue) {
        this.changevalue = changevalue;
    }

    public String getAddvalue() {
        return addvalue;
    }

    public void setAddvalue(String addvalue) {
        this.addvalue = addvalue;
    }

    public String getNewpagevalue() {
        return newpagevalue;
    }

    public void setNewpagevalue(String newpagevalue) {
        this.newpagevalue = newpagevalue;
    }

    public String getNewlinkvalue() {
        return newlinkvalue;
    }

    public void setNewlinkvalue(String newlinkvalue) {
        this.newlinkvalue = newlinkvalue;
    }

    public String getNewwsvalue() {
        return newwsvalue;
    }

    public void setNewwsvalue(String newwsvalue) {
        this.newwsvalue = newwsvalue;
    }

    public String getNewlrvalue() {
        return newlrvalue;
    }

    public void setNewlrvalue(String newlrvalue) {
        this.newlrvalue = newlrvalue;
    }

    public String getReadvalue() {
        return readvalue;
    }

    public void setReadvalue(String readvalue) {
        this.readvalue = readvalue;
    }

    public boolean isDisplayread() {
        return displayread;
    }

    public void setDisplayread(boolean displayread) {
        this.displayread = displayread;
    }

    public boolean isDisplaylink() {
        return displaylink;
    }

    public void setDisplaylink(boolean displaylink) {
        this.displaylink = displaylink;
    }

    public boolean isDisplayrating() {
        return displayrating;
    }

    public void setDisplayrating(boolean displayrating) {
        this.displayrating = displayrating;
    }

    public boolean isDisplaylastread() {
        return displaylastread;
    }

    public void setDisplaylastread(boolean displaylastread) {
        this.displaylastread = displaylastread;
    }

    public boolean isDisplaywaituntil() {
        return displaywaituntil;
    }

    public void setDisplaywaituntil(boolean displaywaituntil) {
        this.displaywaituntil = displaywaituntil;
    }

    public boolean isDisplayreadingstatus() {
        return displayreadingstatus;
    }

    public void setDisplayreadingstatus(boolean displayreadingstatus) {
        this.displayreadingstatus = displayreadingstatus;
    }

    public boolean isDisplaywritingstatus() {
        return displaywritingstatus;
    }

    public void setDisplaywritingstatus(boolean displaywritingstatus) {
        this.displaywritingstatus = displaywritingstatus;
    }

    public List<String[]> getFilters() {
        return filters;
    }

    public void setFilters(List<String[]> filters) {
        this.filters = filters;
    }

    public String getSortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    public String getGroupby() {
        return groupby;
    }

    public void setGroupby(String groupby) {
        this.groupby = groupby;
    }

    public boolean isSortdescending() {
        return sortdescending;
    }

    public void setSortdescending(boolean sortdescending) {
        this.sortdescending = sortdescending;
    }

    public boolean isGroupdescending() {
        return groupdescending;
    }

    public void setGroupdescending(boolean groupdescending) {
        this.groupdescending = groupdescending;
    }
}
