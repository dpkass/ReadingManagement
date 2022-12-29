package Management;

import AppRunner.Datacontainers.Error;
import AppRunner.Datacontainers.Request;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryNotFoundException;
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
                case Wait -> Processor.doWait(rq);
                case Pause -> Processor.doPause(rq);
                case List -> Processor.doList(rq);
                case Recommend -> Processor.doRecommend();
                case Open -> Processor.doOpen(rq);
                case Help -> {}
                default -> throw new IllegalArgumentException("1");
            }
        } catch (EntryNotFoundException | IllegalArgumentException e) {
            int code = Integer.parseInt(e.getMessage());
            String message = Helper.errorMessage(e.getMessage());
            rr.setError(new Error(code, message));
        } catch (Exception e) {
            e.printStackTrace();
            rr.setError(new Error(-1, "BIG ERROR"));
        }
    }
}