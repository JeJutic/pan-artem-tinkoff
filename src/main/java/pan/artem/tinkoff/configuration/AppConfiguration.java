package pan.artem.tinkoff.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.service.cache.WeatherCache;
import pan.artem.tinkoff.service.cache.WeatherCacheImpl;
import pan.artem.tinkoff.service.handler.ExternalServiceResponseErrorHandler;
import pan.artem.tinkoff.properties.AppProperties;

@Configuration
@EnableConfigurationProperties({
        AppProperties.class,
        AppProperties.CurrentWeatherProperties.class,
        AppProperties.CacheProperties.class,
        AppProperties.MovingAverageProperties.class
})
@EnableScheduling
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

    @Bean
    public RestTemplate selfRestTemplate(
            RestTemplateBuilder restTemplateBuilder,
            AppProperties.MovingAverageProperties movingAverageProperties
    ) {
        return restTemplateBuilder
                .rootUri("http://localhost:8080/api")
                .basicAuthentication(
                        "cron",
                        movingAverageProperties.getCronPassword())
                .build();
    }

    @Bean
    public WeatherCache weatherCache(AppProperties.CacheProperties cacheProperties) {
        return new WeatherCacheImpl(cacheProperties.getSize());
    }

    @Bean
    public WeatherCache currentWeatherCache(AppProperties.CacheProperties cacheProperties) {
        return new WeatherCacheImpl(cacheProperties.getSize());
    }

}
