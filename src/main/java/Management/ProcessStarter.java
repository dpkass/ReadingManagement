package Management;

import AppRunner.Data.Containers.Error;
import AppRunner.Data.Containers.Request;
import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryNotFoundException;
import Processing.Processor;
import AppRunner.Data.Containers.RequestResult;

public class ProcessStarter {

    EntryList el;
    EntryList secretel;
    RequestResult rr;

    public ProcessStarter(RequestResult rr) {
        this.rr = rr;
        Processor.setRr(rr);
    }

    void process(Request rq) {
        if (rq == null) return;

        if (rq.secret()) Processor.setEl(secretel);
        else Processor.setEl(el);

        try {
            switch (rq.operator()) {
                case New -> Processor.doNew(rq);
                case Read -> Processor.doRead(rq);
                case ReadTo -> Processor.doReadTo(rq);
                case Change -> Processor.doChange(rq);
                case Wait -> Processor.doWait(rq);
                case Pause -> Processor.doPause(rq);
                case List -> Processor.doList(rq);
                case Recommend -> Processor.doRecommend();
                case Open -> Processor.doOpen(rq);
                case Help -> {}
                default -> throw new IllegalArgumentException("1");
            }
        } catch (EntryNotFoundException e) {
            String message = Helper.errorMessage(e.getCode());
            rr.setError(new Error(e.getCode(), message));
        } catch (IllegalArgumentException e) {
            int code = Integer.parseInt(e.getMessage());
            String message = Helper.errorMessage(code);
            rr.setError(new Error(code, message));
        } catch (Exception e) {
            e.printStackTrace();
            rr.setError(new Error(-1, "BIG ERROR"));
        }
    }

    public void setEl(EntryList el) {
        this.el = el;
    }

    public void setSecretel(EntryList secretel) {
        this.secretel = secretel;
    }
}