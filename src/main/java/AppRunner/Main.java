package AppRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans ({ @ComponentScan ("Management") })
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
