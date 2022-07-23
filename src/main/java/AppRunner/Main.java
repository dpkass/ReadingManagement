package AppRunner;

import Management.Manager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return this::run;
    }

    void run(String[] args) {
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
