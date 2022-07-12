import Management.Manager;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Manager m = new Manager();
        if (args.length > 0)
            m.setFile(args[0]);
        if (args.length > 1)
            m.setSecretfile(args[1]);
        if (args.length > 2)
            m.process(Arrays.stream(args).skip(2).collect(Collectors.joining(" ")));
        else m.run();
    }
}
