package pan.artem.tinkoff.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;
import pan.artem.tinkoff.properties.AppProperties;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CurrentWeatherClientImplTest {

    @Test
    void getCurrentWeather() {
        AppProperties.CurrentWeatherProperties weatherProperties =
                Mockito.mock(AppProperties.CurrentWeatherProperties.class);
        when(weatherProperties.getBaseUrl()).thenReturn("test.ru"); // stubbing
        when(weatherProperties.getApiToken()).thenReturn("123456");
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

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