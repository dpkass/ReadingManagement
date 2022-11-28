package Processing;

import java.net.MalformedURLException;
import java.net.URL;

import static Processing.Processor.rr;

class Opener {

    static void open(String link) {
        try {
            new URL(link);
            rr.setType(RequestResult.RequestResultType.OPEN);
            rr.setString(link);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("5");
        }
    }
}
