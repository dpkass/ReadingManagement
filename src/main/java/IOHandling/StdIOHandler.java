package IOHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StdIOHandler implements IOHandler {
    @Override
    public String read() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String s = in.readLine();
            return s;
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    public void write(String s) {
        System.out.println(s);
    }
}
