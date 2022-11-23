package Processing;

import EntryHandling.Entry.Entry;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class Opener {

    static void open(Entry e) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                URI link = new URI(e.link());
                Desktop.getDesktop().browse(link);
            } catch (URISyntaxException | IOException ex) {
                throw new IllegalArgumentException("5");
            }
        } else {
            throw new IllegalArgumentException("6");
        }
    }
}
