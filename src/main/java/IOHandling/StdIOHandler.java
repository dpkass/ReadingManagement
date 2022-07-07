package IOHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class StdIOHandler implements IOHandler {
    @Override
    public String read() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            return in.readLine();
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    public void write(String s) {
        PrintWriter pw = new PrintWriter(System.out);
        pw.write(s);
        pw.close();
    }
}
