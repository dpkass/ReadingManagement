package AppRunner.Controllers;

import AppRunner.Datacontainers.Request;
import AppRunner.Datacontainers.RequestDummy;
import EntryHandling.Entry.Entry;
import Management.Manager;
import Processing.RequestResult;
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
        return mgr.entries().map(Entry::name).collect(Collectors.toList());
    }

    public RequestResult processCommand(String command) {
        Request rq = Request.parse(command);
        return mgr.process(rq);
    }

    RequestResult processForm(RequestDummy rd) {
        Request rq = rd.toRequest();
        return mgr.process(rq);
    }

    public List<File> files() {
        return List.of(mgr.file(), mgr.secretfile());
    }
}
