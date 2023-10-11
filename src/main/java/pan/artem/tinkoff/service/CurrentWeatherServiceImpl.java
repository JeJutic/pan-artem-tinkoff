package pan.artem.tinkoff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.core.exception.AcquirePermissionCancelledException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.exception.externalservice.ExternalServiceException;
import pan.artem.tinkoff.exception.RequestNotPermitted;
import pan.artem.tinkoff.dto.WeatherDto;

import java.time.Instant;

public class CurrentWeatherServiceImpl implements CurrentWeatherService {

    @Value("${app.properties.weather-api-token}")
    private String weatherApiToken;
    private final RestTemplate restTemplate;
    private final RateLimiter rateLimiter;

    public CurrentWeatherServiceImpl(RestTemplate restTemplate, RateLimiter rateLimiter) {
        this.restTemplate = restTemplate;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public WeatherDto getCurrentWeather(String city) {
        String weatherJson;
        try {
            weatherJson = rateLimiter.executeCallable(() -> restTemplate.getForObject(
                    "https://api.weatherapi.com/v1/current.json?key={apiKey}&q={city}",
                    String.class,
                    weatherApiToken,
                    city
            ));
        } catch (io.github.resilience4j.ratelimiter.RequestNotPermitted | AcquirePermissionCancelledException e) {
            throw new RequestNotPermitted(e);
        } catch (Exception e) {
            throw new ExternalServiceException(
                    "Exception occurred when executing Weather API call", e
            );
        }

        int temp;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(weatherJson);
            JsonNode current = root.path("current");
            temp = current.path("temp_c").asInt();
        } catch (JsonProcessingException e) {
            throw new ExternalServiceException(
                    "Exception occurred when parsing Weather API response", e
            );
        }

        return new WeatherDto(temp, Instant.now());
    }
}
