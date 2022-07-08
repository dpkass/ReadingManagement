import Management.Manager;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Manager m = new Manager();
        if (args.length > 0)
            m.setFile(args[0]);
        if (args.length > 1)
            m.process(Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
        else m.run();
    }
}
