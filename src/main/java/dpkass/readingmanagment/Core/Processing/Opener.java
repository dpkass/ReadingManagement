package dpkass.readingmanagment.Core.Processing;

import dpkass.readingmanagment.Domain.Aggregates.RequestResult;

import java.net.MalformedURLException;
import java.net.URL;

class Opener {

    static void open(String link) {
        try {
            new URL(link);
            Processor.rr.setType(RequestResult.RequestResultType.OPEN);
            Processor.rr.setString(link);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("5");
        }
    }
}
