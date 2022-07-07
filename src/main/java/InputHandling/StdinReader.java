package InputHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StdinReader implements InputReader {
    @Override
    public String read() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            return in.readLine();
        } catch (IOException ignored) {
            return null;
        }
    }
}
