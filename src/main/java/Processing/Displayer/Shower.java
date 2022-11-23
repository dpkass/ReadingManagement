package Processing.Displayer;

import EntryHandling.Entry.Entry;
import Processing.RequestResult;
import Processing.TableDataSupplier;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static Processing.Processor.rr;

class Shower {
    static void show(Entry e, List<String> parts) {
        Stream<String> partsStream;
        if (parts.size() == 2) partsStream = showAll();
        else partsStream = parts.stream().skip(2);

        TableDataSupplier tds = new TableDataSupplier();

        partsStream.map(DisplayUtil::getFunction).forEach(tds::add);

        rr.setDatasupplier(tds);
        rr.setList(Collections.singletonList(e));
        rr.setType(RequestResult.RequestResultType.SHOW);
    }

    // needs rework
    private static Stream<String> showAll() {
        return DisplayUtil.allAttributes();
    }
}
