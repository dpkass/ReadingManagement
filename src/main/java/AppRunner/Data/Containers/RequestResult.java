package AppRunner.Data.Containers;

import EntryHandling.Entry.Entry;
import Processing.TableDataSupplier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RequestResult {

    public enum RequestResultType {
        SUCCESS, LIST, GROUPEDLIST, OPEN, HELP
    }

    public RequestResult() {}

    public RequestResult(Error error, RequestResultType type, String string, TableDataSupplier datasupplier, List<String> headerlist, List<Entry> list, Map<?, List<Entry>> groupedmap) {
        this.error = error;
        this.type = type;
        this.string = string;
        this.datasupplier = datasupplier;
        this.headerlist = headerlist;
        this.list = list;
        this.groupedmap = groupedmap;
    }

    public RequestResult copy() {
        RequestResult rr = new RequestResult(error, type, string, datasupplier, headerlist, list, groupedmap);
        clear();
        return rr;
    }

    private void clear() {
        this.error = null;
        this.type = null;
        this.string = null;
        this.datasupplier = null;
        this.headerlist = null;
        this.list = null;
        this.groupedmap = null;
    }

    public boolean hasError() {
        return error != null;
    }

    Error error;
    RequestResultType type;
    String string;
    TableDataSupplier datasupplier;
    List<String> headerlist;
    List<Entry> list;
    Map<?, List<Entry>> groupedmap;


    public Error error() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public RequestResultType type() {
        return type;
    }

    public void setType(RequestResultType type) {
        this.type = type;
    }

    public String string() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public List<Function<Entry, String>> datafunctions() {
        return datasupplier.suppliers();
    }

    public void setDatasupplier(TableDataSupplier datasupplier) {
        this.datasupplier = datasupplier;
    }

    public List<String> headerlist() {
        return headerlist;
    }

    public void setHeaderlist(List<String> headerlist) {
        this.headerlist = headerlist;
    }

    public List<Entry> list() {
        return list;
    }

    public void setList(List<Entry> list) {
        this.list = list;
    }

    public Map<?, List<Entry>> groupedmap() {
        return groupedmap;
    }

    public void setGroupedmap(Map<?, List<Entry>> groupedmap) {
        this.groupedmap = groupedmap;
    }
}
