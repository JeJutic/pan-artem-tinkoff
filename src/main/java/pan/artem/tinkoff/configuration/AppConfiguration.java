package pan.artem.tinkoff.configuration;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.controller.error.externalservice.ExternalServiceResponseErrorHandler;
import pan.artem.tinkoff.service.CurrentWeatherService;
import pan.artem.tinkoff.service.CurrentWeatherServiceImpl;

import java.time.Duration;

@Configuration
public class AppConfiguration {

    @Bean
    public RestTemplate currentWeatherRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .errorHandler(new ExternalServiceResponseErrorHandler())
                .build();
    }

    @Bean
    public RateLimiter currentWeatherRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(5))
                .timeoutDuration(Duration.ofMillis(100))
                .limitForPeriod(5)
                .build();

        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        return rateLimiterRegistry.rateLimiter("currentWeather");
    }

    @Bean
    public CurrentWeatherService currentWeatherService(
            @Qualifier("currentWeatherRestTemplate") RestTemplate restTemplate,
            @Qualifier("currentWeatherRateLimiter") RateLimiter rateLimiter
    ) {
        return new CurrentWeatherServiceImpl(restTemplate, rateLimiter);
    }
}
