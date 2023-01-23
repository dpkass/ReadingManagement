package dpkass.readingmanagment.WebService;

import dpkass.readingmanagment.Domain.Aggregates.Request;
import dpkass.readingmanagment.Domain.Aggregates.RequestResult;
import dpkass.readingmanagment.WebService.DataContainers.RequestDummy;
import dpkass.readingmanagment.Domain.Aggregates.Book;
import dpkass.readingmanagment.Core.Management.Manager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {
    Manager mgr;

    public RequestService(Manager mgr) {
        this.mgr = mgr;
        mgr.load();
    }

    public List<String> books() {
        return mgr.entries().map(Book::name).collect(Collectors.toList());
    }

    public Object secretbooks() {
        return mgr.secretentries().map(Book::name).collect(Collectors.toList());
    }

    public RequestResult processForm(RequestDummy rd) {
        Request rq = rd.toRequest();
        return mgr.process(rq);
    }

    public List<File> files() {
        return List.of(mgr.file(), mgr.secretfile());
    }
}
