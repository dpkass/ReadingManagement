package AppRunner.Datastructures;

public final class Request {
    private Operator operator;
    private String operator2;
    private String book;
    private DisplayElements de;
    private String sortby;
    private String groupby;

    public static Request standard() {
        return new Request();
    }

    public String asCommand() {
        return "%s %s \"%s\" %s sortby=%s groupby=%s".formatted(operator, operator2, book, de != null ? de.asCommand() : "", sortby, groupby);
    }

    @Override
    public String toString() {
        return "Request[" +
                "operator=" + operator + ", " +
                "book=" + book + ", " +
                "de=" + de + ", " +
                "sortby=" + sortby + ", " +
                "groupby=" + groupby + ']';
    }

    // getters and setters


    public Operator operator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = Operator.getWithDisplayValue(operator);
    }

    public String operator2() {
        return operator2;
    }

    public void setOperator2(String operator2) {
        this.operator2 = operator2;
    }

    public String book() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public DisplayElements de() {
        return de;
    }

    public void setDe(DisplayElements de) {
        this.de = de;
    }

    public String sortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    public String groupby() {
        return groupby;
    }

    public void setGroupby(String groupby) {
        this.groupby = groupby;
    }
}
