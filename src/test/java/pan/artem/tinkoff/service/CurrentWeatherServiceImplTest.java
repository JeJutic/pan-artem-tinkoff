package pan.artem.tinkoff.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.dto.externalservice.ConditionDto;
import pan.artem.tinkoff.dto.externalservice.CurrentDto;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;
import pan.artem.tinkoff.properties.AppProperties;
import pan.artem.tinkoff.service.cache.WeatherCache;
import pan.artem.tinkoff.service.client.CurrentWeatherClient;
import pan.artem.tinkoff.service.client.CurrentWeatherClientImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CurrentWeatherServiceImplTest {

    private AppProperties.CurrentWeatherProperties weatherProperties;
    private RestTemplate restTemplate;

    @BeforeEach
    void init() {
        weatherProperties = Mockito.mock(AppProperties.CurrentWeatherProperties.class);
        restTemplate = Mockito.mock(RestTemplate.class);
    }

    @Test
    void getCurrentWeather() {
        when(restTemplate.getForObject(any(), any(), any(), any()))
                .thenReturn(new CurrentWeatherDto(
                        new CurrentDto(10, new ConditionDto("rainy"))
                ));

        CurrentWeatherClient weatherClient = new CurrentWeatherClientImpl(
                weatherProperties,
                restTemplate
        );
        CurrentWeatherServiceImpl weatherService = new CurrentWeatherServiceImpl(
                weatherClient,
                null,
                Mockito.mock(WeatherCache.class)
        );
        weatherService = Mockito.spy(weatherService);

        var weatherDto = weatherService.getCurrentWeather("Saint-Petersburg");

        Assertions.assertEquals(weatherDto.temperature(), 10);
        Assertions.assertEquals(weatherDto.weatherType(), "rainy");
        verify(weatherService).getCurrentWeather("Saint-Petersburg");
    }
}