package IOHandling;

import java.util.ArrayList;
import java.util.List;

public class SavingIOHandler implements IOHandler {

    List<String> out = new ArrayList<>();

    @Override
    public String read() {
        return null;
    }

    @Override
    public void write(String message) {
        out.add(message);
    }

    public List<String> out() {
        return out;
    }

    public void clear() {
        out.clear();
    }
}
