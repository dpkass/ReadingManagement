package AppRunner;

import AppRunner.Validation.RequestValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    @Bean
    @Qualifier ("requestValidator")
    public RequestValidator validator() {
        return new RequestValidator();
    }
}
