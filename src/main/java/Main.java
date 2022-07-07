import Management.Manager;

public class Main {
    public static void main(String[] args) {
        Manager m = new Manager();
        if (args.length == 1)
            m.setFile(args[0]);
        m.run();
    }
}
