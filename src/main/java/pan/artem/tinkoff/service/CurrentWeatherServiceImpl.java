package pan.artem.tinkoff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.controller.error.weatherapi.WeatherApiException;
import pan.artem.tinkoff.controller.error.weatherapi.WeatherApiResponseErrorHandler;
import pan.artem.tinkoff.dto.WeatherDto;

import java.time.Instant;

@Service
public class CurrentWeatherServiceImpl implements CurrentWeatherService {

    private final RestTemplate restTemplate;

    @Autowired
    public CurrentWeatherServiceImpl(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate
                .errorHandler(new WeatherApiResponseErrorHandler())
                .build();
    }

    @Override
    public WeatherDto getCurrentWeather(String city) {
        String weatherJson = restTemplate.getForObject(
                "https://api.weatherapi.com/v1/current.json?key={apiKey}&q={city}",
                String.class,
                "cc7c19a929d8471dbc5124457230610",
                city
        );

        int temp;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(weatherJson);
            JsonNode current = root.path("current");
            temp = current.path("temp_c").asInt();
        } catch (JsonProcessingException e) {
            throw new WeatherApiException(e);
        }

        return new WeatherDto(temp, Instant.now());
    }
}
