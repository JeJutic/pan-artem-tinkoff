package pan.artem.tinkoff.service;

import io.github.resilience4j.core.exception.AcquirePermissionCancelledException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;
import pan.artem.tinkoff.exception.RequestNotPermitted;
import pan.artem.tinkoff.exception.externalservice.ExternalServiceException;
import pan.artem.tinkoff.properties.AppProperties;

@Service
public class CurrentWeatherClientImpl implements CurrentWeatherClient {

    private final AppProperties.CurrentWeatherProperties currentWeatherProperties;
    private final RestTemplate restTemplate;

    public CurrentWeatherClientImpl(
            AppProperties.CurrentWeatherProperties currentWeatherProperties,
            @Qualifier("currentWeatherRestTemplate") RestTemplate restTemplate
    ) {
        this.currentWeatherProperties = currentWeatherProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    @RateLimiter(name = "current-weather", fallbackMethod = "getCurrentWeatherFallback")
    public CurrentWeatherDto getCurrentWeather(String city) {
            return restTemplate.getForObject(
                    "/current.json?key={apiKey}&q={city}",
                    CurrentWeatherDto.class,
                    currentWeatherProperties.getApiToken(),
                    city
            );
    }

    @SuppressWarnings("unused")
    private CurrentWeatherDto getCurrentWeatherFallback(
            String city,
            io.github.resilience4j.ratelimiter.RequestNotPermitted e
    ) {
        throw new RequestNotPermitted(e);
    }

    @SuppressWarnings("unused")
    private CurrentWeatherDto getCurrentWeatherFallback(
            String city,
            AcquirePermissionCancelledException e
    ) {
        throw new RequestNotPermitted(e);
    }

    @SuppressWarnings("unused")
    private CurrentWeatherDto getCurrentWeatherFallback(String city, Exception e) {
        throw new ExternalServiceException(
                "Exception occurred when executing Weather API call", e
        );
    }
}
