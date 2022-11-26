package Management;

import AppRunner.Datastructures.Error;
import AppRunner.Datastructures.Request;
import EntryHandling.Entry.EntryList;
import Processing.Processor;
import Processing.RequestResult;

public class ProcessStarter {

    final EntryList el;
    final EntryList secretel;
    RequestResult rr;

    public ProcessStarter(EntryList el, EntryList secretel, RequestResult rr) {
        this.el = el;
        this.secretel = secretel;
        this.rr = rr;
        Processor.setRr(rr);
    }

    void process(Request rq) {
        if (rq == null) return;

        Processor.setEl(el);

        try {
            switch (rq.operator()) {
                case New -> Processor.doNew(rq);
                case Read -> Processor.doRead(rq);
                case ReadTo -> Processor.doReadTo(rq);
                case Add -> Processor.doAdd(rq);
                case Change -> Processor.doChange(rq);
                case List -> Processor.doList(rq);
                case Show -> Processor.doShow(rq);
                case ListAll -> Processor.doListAll();
                case Recommend -> Processor.doRecommend();
                case Open -> Processor.doOpen(rq);
                case Help -> doHelp(rq);
                default -> throw new IllegalArgumentException("1");
            }
        } catch (Exception e) {
            int code = Integer.parseInt(e.getMessage());
            String message = Helper.errorMessage(e.getMessage());
            rr.setError(new Error(code, message));
        }
    }

    private void doHelp(Request rq) {
        rr.setString(Helper.help(rq.helpoperator()));
        rr.setType(RequestResult.RequestResultType.HELP);
    }

//    void doSecret(List<String> rq) {
//        rq = rq.subList(1, rq.size());
//        Processor.setEl(secretel);
//        process(rq);
//    }
}