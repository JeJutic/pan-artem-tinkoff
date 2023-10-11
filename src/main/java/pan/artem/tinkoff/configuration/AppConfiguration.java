package pan.artem.tinkoff.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.service.handler.ExternalServiceResponseErrorHandler;
import pan.artem.tinkoff.properties.AppProperties;

@Configuration
@EnableConfigurationProperties({AppProperties.class, AppProperties.CurrentWeatherProperties.class})
public class AppConfiguration {

    @Bean
    public RestTemplate currentWeatherRestTemplate(
            RestTemplateBuilder restTemplateBuilder,
            AppProperties.CurrentWeatherProperties currentWeatherProperties
    ) {
        return restTemplateBuilder
                .rootUri(currentWeatherProperties.getBaseUrl())
                .errorHandler(new ExternalServiceResponseErrorHandler())
                .build();
    }

}
