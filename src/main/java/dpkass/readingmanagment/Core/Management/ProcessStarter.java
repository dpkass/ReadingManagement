package dpkass.readingmanagment.Core.Management;

import dpkass.readingmanagment.Core.Processing.Processor;
import dpkass.readingmanagment.Domain.Aggregates.Request;
import dpkass.readingmanagment.Domain.Aggregates.RequestParts.Error;
import dpkass.readingmanagment.Domain.Aggregates.RequestResult;
import dpkass.readingmanagment.Domain.Exceptions.EntryNotFoundException;
import dpkass.readingmanagment.Persistence.EntryRepository;

public class ProcessStarter {

    EntryRepository repo;
    EntryRepository secretrepo;
    RequestResult rr;

    public ProcessStarter(RequestResult rr) {
        this.rr = rr;
        Processor.setRr(rr);
    }

    void process(Request rq) {
        if (rq == null) return;

        if (rq.secret()) Processor.setEl(secretrepo);
        else Processor.setEl(repo);

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

    public void setRepo(EntryRepository repo) {
        this.repo = repo;
    }

    public void setSecretrepo(EntryRepository secretrepo) {
        this.secretrepo = secretrepo;
    }
}