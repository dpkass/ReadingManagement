package Processing.Displayer;

import AppRunner.Datastructures.DisplayAttributesForm;
import AppRunner.Datastructures.DisplayAttributesUtil;
import EntryHandling.Entry.Entry;
import Processing.RequestResult;
import Processing.TableDataSupplier;

import java.util.Collections;
import java.util.stream.Stream;

import static Processing.Processor.rr;

class Shower {
    static void show(Entry e, DisplayAttributesForm daf) {
        Stream<String> partsStream;
        if (daf.isEmpty()) daf = DisplayAttributesForm.all();

        TableDataSupplier tds = new TableDataSupplier();
        DisplayAttributesUtil.stream(daf).map(DisplayUtil::getFunction).forEach(tds::add);

        rr.setDatasupplier(tds);
        rr.setList(Collections.singletonList(e));
        rr.setType(RequestResult.RequestResultType.SHOW);
    }
}
