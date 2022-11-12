package Management.Processors;

import EntryHandling.Entry.Entry;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Opener {

    public static void open(Entry e) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                URI link = new URI(e.link());
                Desktop.getDesktop().browse(link);
            } catch (URISyntaxException | IOException ex) {
                throw new IllegalArgumentException("link wrong" + '\n' + e.name() + " -> " + e.link());
            }
        } else {
            throw new IllegalArgumentException("no os support" + '\n' + e.name() + " -> " + e.link());
        }
    }
}
