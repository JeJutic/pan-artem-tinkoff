package pan.artem.tinkoff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.core.exception.AcquirePermissionCancelledException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.controller.error.weatherapi.WeatherApiException;
import pan.artem.tinkoff.controller.error.RequestNotPermitted;
import pan.artem.tinkoff.dto.WeatherDto;

import java.time.Instant;

public class CurrentWeatherServiceImpl implements CurrentWeatherService {

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
                    "cc7c19a929d8471dbc5124457230610",
                    city
            ));
        } catch (io.github.resilience4j.ratelimiter.RequestNotPermitted | AcquirePermissionCancelledException e) {
            throw new RequestNotPermitted(e);
        } catch (Exception e) {
            throw new WeatherApiException(
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
            throw new WeatherApiException(
                    "Exception occurred when parsing Weather API response", e
            );
        }

        return new WeatherDto(temp, Instant.now());
    }
}
