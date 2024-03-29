package dpkass.readingmanagment.WebService.DataContainers;

import dpkass.readingmanagment.Domain.Aggregates.Request;
import dpkass.readingmanagment.Domain.Aggregates.RequestParts.ChangeForm;
import dpkass.readingmanagment.Domain.Aggregates.RequestParts.ChangeValueWrapper;
import dpkass.readingmanagment.Domain.Aggregates.RequestParts.DisplayAttributesForm;
import dpkass.readingmanagment.Domain.Aggregates.RequestParts.Filter;
import dpkass.readingmanagment.Domain.Types.Attribute;
import dpkass.readingmanagment.Domain.Types.Genre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dpkass.readingmanagment.Domain.Types.Attribute.*;

public class RequestDummy {
    private boolean secret;
    private String operator = "";
    private String booknew;
    private String booksel = "";

    // addtional change
    private List<ChangeValueWrapper> changevalues = ChangeForm.initialvalues();
    // addtional add
    private String addvalue = "";
    // addtional new
    private String newpagevalue = "0";
    private String newlinkvalue = "";
    private String newwsvalue = "";
    private String newlrvalue;
    private String newbooktypevalue = "";
    private List<String> newgenresvalue;
    // additional read
    private String readvalue = "1";
    // additional wait
    private String waituntil;

    // list display
    private List<String> daf = new ArrayList<>();

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

        LocalDateTime lr = toLDT(newlrvalue);
        LocalDate wu = toLD(waituntil);
        float newpageval = Float.parseFloat(newpagevalue);
        float readval = Float.parseFloat(readvalue);
        Set<Filter<?>> filters = createFilters();
        List<Genre> genres = newgenresvalue.stream().map(Genre::getGenre).collect(Collectors.toList());

        request.setSecret(secret);
        request.setOperator(operator);
        request.setBooknew(booknew);
        request.setBooksel(booksel);
        request.setChangeform(new ChangeForm(changevalues));
        request.setAddvalue(addvalue);
        request.setNewpagevalue(newpageval);
        request.setNewlinkvalue(newlinkvalue);
        request.setNewwsvalue(newwsvalue);
        request.setNewlrvalue(lr);
        request.setNewgenresvalue(genres);
        request.setNewbooktypevalue(newbooktypevalue);
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
        if (!filterbook.isBlank()) filters.add(Filter.createFilter(Name, "=", filterbook));
        if (!filterchapterop.isBlank()) filters.add(Filter.createFilter(ReadTo, filterchapterop, filterchapter));
        if (!filterratingop.isBlank()) filters.add(Filter.createFilter(Rating, filterratingop, filterrating));
        if (!filterlrop.isBlank()) filters.add(Filter.createFilter(LastRead, filterlrop, filterlr));
        if (!filterwuop.isBlank()) filters.add(Filter.createFilter(Attribute.WaitUntil, filterwuop, filterwu));
        if (!filterrs.isEmpty()) filters.add(Filter.createFilter(ReadingStatus, "=", filterrs));
        if (!filterws.isEmpty()) filters.add(Filter.createFilter(WritingStatus, "=", filterws));
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
        return "RequestDummy{" + "secret=" + secret + ", operator='" + operator + '\'' + ", booknew='" + booknew + '\'' + ", booksel='" + booksel + '\'' + ", changevalues=" + changevalues + ", addvalue='" + addvalue + '\'' + ", newpagevalue='" + newpagevalue + '\'' + ", newlinkvalue='" + newlinkvalue + '\'' + ", newwsvalue='" + newwsvalue + '\'' + ", newlrvalue='" + newlrvalue + '\'' + ", newbooktypevalue='" + newbooktypevalue + '\'' + ", newgenresvalue=" + newgenresvalue + ", readvalue='" + readvalue + '\'' + ", waituntil='" + waituntil + '\'' + ", daf=" + daf + ", sortby='" + sortby + '\'' + ", groupby='" + groupby + '\'' + ", sortdescending=" + sortdescending + ", groupdescending=" + groupdescending + ", filterbook='" + filterbook + '\'' + ", filterchapterop='" + filterchapterop + '\'' + ", filterchapter=" + filterchapter + ", filterratingop='" + filterratingop + '\'' + ", filterrating=" + filterrating + ", filterlrop='" + filterlrop + '\'' + ", filterlr='" + filterlr + '\'' + ", filterwuop='" + filterwuop + '\'' + ", filterwu='" + filterwu + '\'' + ", filterrs=" + filterrs + ", filterws=" + filterws + '}';
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

    public List<ChangeValueWrapper> getChangevalues() {
        return changevalues;
    }

    public void setChangevalues(List<ChangeValueWrapper> changevalues) {
        this.changevalues = changevalues;
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

    public String getNewbooktypevalue() {
        return newbooktypevalue;
    }

    public void setNewbooktypevalue(String newbooktypevalue) {
        this.newbooktypevalue = newbooktypevalue;
    }

    public List<String> getNewgenresvalue() {
        return newgenresvalue;
    }

    public void setNewgenresvalue(List<String> newgenresvalue) {
        this.newgenresvalue = newgenresvalue;
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

    public List<String> getDaf() {
        return daf;
    }

    public void setDaf(List<String> daf) {
        this.daf = daf;
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
