package hexlet.code.app.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

import static hexlet.code.app.configuration.SpringConfigTests.TEST_PROFILE;

@Configuration
@Profile(TEST_PROFILE)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "hexlet.code.app")
@PropertySource(value = "classpath:application.yml")
public class SpringConfigTests {

    public static final String TEST_PROFILE = "test";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

}
