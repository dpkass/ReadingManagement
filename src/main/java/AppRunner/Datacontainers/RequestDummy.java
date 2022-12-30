package AppRunner.Datacontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static AppRunner.Datacontainers.Attribute.*;

public final class RequestDummy {
    private boolean secret;
    private String operator = "";
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
    private String newlrvalue;
    // additional read
    private String readvalue = "1";
    // additional wait
    private String waituntil;

    // list display
    private boolean displayread;
    private boolean displaylink;
    private boolean displayrating;
    private boolean displaylastread;
    private boolean displaywaituntil;
    private boolean displayreadingstatus;
    private boolean displaywritingstatus;

    // order args
    private String sortby;
    private String groupby;
    private boolean sortdescending;
    private boolean groupdescending;

    // filter args
    private String filterbook;
    private String filterchapterop;
    private float filterchapter;
    private String filterratingop;
    private float filterrating;
    private String filterlrop;
    private String filterlr;
    private String filterwuop;
    private String filterwu;
    private List<String> filterrs;
    private List<String> filterws;

    public Request toRequest() {
        Request request = new Request();

        List<Boolean> daf = java.util.List.of(displayread, displaylink, displayrating, displaylastread, displaywaituntil, displayreadingstatus, displaywritingstatus);

        LocalDateTime lr = toLDT(newlrvalue);
        LocalDate wu = toLD(waituntil);
        float newpageval = Float.parseFloat(newpagevalue);
        float readval = Float.parseFloat(readvalue);
        Set<Filter<?>> filters = createFilters();

        request.setSecret(secret);
        request.setOperator(operator);
        request.setBooknew(booknew);
        request.setBooksel(booksel);
        request.setChangeattribute(changeattribute);
        request.setChangevalue(changevalue);
        request.setAddvalue(addvalue);
        request.setNewpagevalue(newpageval);
        request.setNewlinkvalue(newlinkvalue);
        request.setNewwsvalue(newwsvalue);
        request.setNewlrvalue(lr);
        request.setReadvalue(readval);
        request.setWaituntil(wu);
        request.setDaf(new DisplayAttributesForm(daf));
        request.setSortby(sortby);
        request.setGroupby(groupby);
        request.setSortdescending(sortdescending);
        request.setGroupdescending(groupdescending);
        request.setFilters(filters);

        return request;
    }

    public Set<Filter<?>> createFilters() {
        Set<Filter<?>> filters = new HashSet<>();
        if (!filterbook.isBlank()) filters.add(Filter.createFilter(name, "=", filterbook));
        if (!filterchapterop.isBlank()) filters.add(Filter.createFilter(readto, filterchapterop, filterchapter));
        if (!filterratingop.isBlank()) filters.add(Filter.createFilter(rating, filterratingop, filterrating));
        if (!filterlrop.isBlank()) filters.add(Filter.createFilter(lastread, filterlrop, filterlr));
        if (!filterwuop.isBlank()) filters.add(Filter.createFilter(Attribute.waituntil, filterwuop, filterwu));
        if (!filterrs.isEmpty()) filters.add(Filter.createFilter(readingStatus, "=", filterrs));
        if (!filterws.isEmpty()) filters.add(Filter.createFilter(writingStatus, "=", filterws));
        return filters;
    }

    private LocalDateTime toLDT(String ldt) {
        try {
            return LocalDateTime.parse(ldt);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    private LocalDate toLD(String ld) {
        try {
            return LocalDate.parse(ld);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    public static RequestDummy standard() {
        return new RequestDummy();
    }

    @Override
    public String toString() {
        return "RequestDummy{" + "operator='" + operator + '\'' + ", booknew='" + booknew + '\'' + ", booksel='" + booksel + '\'' + ", changeattribute='" + changeattribute + '\'' + ", changevalue='" + changevalue + '\'' + ", addvalue='" + addvalue + '\'' + ", newpagevalue='" + newpagevalue + '\'' + ", newlinkvalue='" + newlinkvalue + '\'' + ", newwsvalue='" + newwsvalue + '\'' + ", newlrvalue='" + newlrvalue + '\'' + ", readvalue='" + readvalue + '\'' + ", waituntil='" + waituntil + '\'' + ", displayread=" + displayread + ", displaylink=" + displaylink + ", displayrating=" + displayrating + ", displaylastread=" + displaylastread + ", displaywaituntil=" + displaywaituntil + ", displayreadingstatus=" + displayreadingstatus + ", displaywritingstatus=" + displaywritingstatus + ", sortby='" + sortby + '\'' + ", groupby='" + groupby + '\'' + ", sortdescending=" + sortdescending + ", groupdescending=" + groupdescending + ", filterbook='" + filterbook + '\'' + ", filterchapterop='" + filterchapterop + '\'' + ", filterchapter=" + filterchapter + ", filterratingop='" + filterratingop + '\'' + ", filterrating=" + filterrating + ", filterlrop='" + filterlrop + '\'' + ", filterlr='" + filterlr + '\'' + ", filterwuop='" + filterwuop + '\'' + ", filterwu='" + filterwu + '\'' + ", filterrs=" + filterrs + ", filterws=" + filterws + '}';
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
    public boolean isSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public String getWaituntil() {
        return waituntil;
    }

    public void setWaituntil(String waituntil) {
        this.waituntil = waituntil;
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

    public String getFilterbook() {
        return filterbook;
    }

    public void setFilterbook(String filterbook) {
        this.filterbook = filterbook;
    }

    public String getFilterchapterop() {
        return filterchapterop;
    }

    public void setFilterchapterop(String filterchapterop) {
        this.filterchapterop = filterchapterop;
    }

    public float getFilterchapter() {
        return filterchapter;
    }

    public void setFilterchapter(float filterchapter) {
        this.filterchapter = filterchapter;
    }

    public String getFilterratingop() {
        return filterratingop;
    }

    public void setFilterratingop(String filterratingop) {
        this.filterratingop = filterratingop;
    }

    public float getFilterrating() {
        return filterrating;
    }

    public void setFilterrating(float filterrating) {
        this.filterrating = filterrating;
    }

    public String getFilterlrop() {
        return filterlrop;
    }

    public void setFilterlrop(String filterlrop) {
        this.filterlrop = filterlrop;
    }

    public String getFilterlr() {
        return filterlr;
    }

    public void setFilterlr(String filterlr) {
        this.filterlr = filterlr;
    }

    public String getFilterwuop() {
        return filterwuop;
    }

    public void setFilterwuop(String filterwuop) {
        this.filterwuop = filterwuop;
    }

    public String getFilterwu() {
        return filterwu;
    }

    public void setFilterwu(String filterwu) {
        this.filterwu = filterwu;
    }

    public List<String> getFilterrs() {
        return filterrs;
    }

    public void setFilterrs(List<String> filterrs) {
        this.filterrs = filterrs;
    }

    public List<String> getFilterws() {
        return filterws;
    }

    public void setFilterws(List<String> filterws) {
        this.filterws = filterws;
    }
}