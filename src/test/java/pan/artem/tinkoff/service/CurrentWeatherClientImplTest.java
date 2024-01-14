package pan.artem.tinkoff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;
import pan.artem.tinkoff.properties.AppProperties;
import pan.artem.tinkoff.service.client.CurrentWeatherClientImpl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CurrentWeatherClientImplTest {

    private AppProperties.CurrentWeatherProperties weatherProperties;
    private RestTemplate restTemplate;

    @BeforeEach
    void init() {
        weatherProperties = Mockito.mock(AppProperties.CurrentWeatherProperties.class);
        restTemplate = Mockito.mock(RestTemplate.class);
    }

    @Test
    void getCurrentWeather() {
        when(weatherProperties.getBaseUrl()).thenReturn("test.ru"); // stubbing
        when(weatherProperties.getApiToken()).thenReturn("123456");

        CurrentWeatherClientImpl weatherClient =
                new CurrentWeatherClientImpl(weatherProperties, restTemplate);
        weatherClient.getCurrentWeather("Moscow");

        verify(restTemplate).getForObject(
                "/current.json?key={apiKey}&q={city}",
                CurrentWeatherDto.class,
                "123456",
                "Moscow"
        );  // mocking
    }
}