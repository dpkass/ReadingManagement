package AppRunner.Datacontainers;

import EntryHandling.Entry.WritingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class Request {
    private Operator operator;
    private String booknew;
    private String booksel;

    // addtional change
    private Attribute changeattribute;
    private String changevalue;
    // addtional add
    private String addvalue;
    // addtional new
    private float newpagevalue;
    private String newlinkvalue;
    private WritingStatus newwsvalue;
    private LocalDateTime newlrvalue;
    // additional read
    private float readvalue;
    // additional wait
    private LocalDate waituntil;

    // list display
    private DisplayAttributesForm daf;
    private List<Filter<?>> filters;

    // order args
    private Attribute sortby;
    private Attribute groupby;
    private boolean sortdescending;
    private boolean groupdescending;

    public static Request parse(String command) {
        return RequestBuilder.build(command);
    }

    @Override
    public String toString() {
        return "Request{" + "operator=" + operator + ", booknew='" + booknew + '\'' + ", booksel='" + booksel + '\'' + ", changeattribute=" + changeattribute + ", changevalue='" + changevalue + '\'' + ", addvalue='" + addvalue + '\'' + ", newpagevalue=" + newpagevalue + ", newlinkvalue='" + newlinkvalue + '\'' + ", newwsvalue=" + newwsvalue + ", newlrvalue=" + newlrvalue + ", readvalue=" + readvalue + ", waituntil=" + waituntil + ", daf=" + daf + ", filters=" + filters + ", sortby=" + sortby + ", groupby=" + groupby + ", sortdescending=" + sortdescending + ", groupdescending=" + groupdescending + '}';
    }

    // getters and setters
    public Operator operator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setOperator(String operator) {
        this.operator = Operator.getOperator(operator);
    }

    public String booknew() {
        return booknew;
    }

    public void setBooknew(String booknew) {
        this.booknew = booknew;
    }

    public String booksel() {
        return booksel;
    }

    public void setBooksel(String booksel) {
        this.booksel = booksel;
    }

    public DisplayAttributesForm daf() {
        return daf;
    }

    public void setDaf(DisplayAttributesForm daf) {
        this.daf = daf;
    }

    public List<Filter<?>> filters() {
        return filters;
    }

    public void setFilters(List<Filter<?>> filters) {
        this.filters = filters;
    }

    public Attribute sortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = Attribute.getAttribute(sortby);
    }

    public Attribute groupby() {
        return groupby;
    }

    public void setGroupby(String groupby) {
        this.groupby = Attribute.getAttribute(groupby);
    }

    public Attribute changeattribute() {
        return changeattribute;
    }

    public void setChangeattribute(String changeattribute) {
        this.changeattribute = Attribute.getAttribute(changeattribute);
    }

    public String changevalue() {
        return changevalue;
    }

    public void setChangevalue(String changevalue) {
        this.changevalue = changevalue;
    }

    public String addvalue() {
        return addvalue;
    }

    public void setAddvalue(String addvalue) {
        this.addvalue = addvalue;
    }

    public float newpagevalue() {
        return newpagevalue;
    }

    public void setNewpagevalue(float newpagevalue) {
        this.newpagevalue = newpagevalue;
    }

    public String newlinkvalue() {
        return newlinkvalue;
    }

    public void setNewlinkvalue(String newlinkvalue) {
        this.newlinkvalue = newlinkvalue;
    }

    public WritingStatus newwsvalue() {
        return newwsvalue;
    }

    public void setNewwsvalue(String newwsvalue) {
        this.newwsvalue = WritingStatus.getStatus(newwsvalue);
    }

    public LocalDateTime newlrvalue() {
        return newlrvalue;
    }

    public void setNewlrvalue(LocalDateTime newlrvalue) {
        this.newlrvalue = newlrvalue;
    }

    public float readvalue() {
        return readvalue;
    }

    public void setReadvalue(float readvalue) {
        this.readvalue = readvalue;
    }

    public LocalDate waituntil() {
        return waituntil;
    }

    public void setWaituntil(LocalDate waituntil) {
        this.waituntil = waituntil;
    }

    public boolean sortdescending() {
        return sortdescending;
    }

    public void setSortdescending(boolean sortdescending) {
        this.sortdescending = sortdescending;
    }

    public boolean groupdescending() {
        return groupdescending;
    }

    public void setGroupdescending(boolean groupdescending) {
        this.groupdescending = groupdescending;
    }
}
