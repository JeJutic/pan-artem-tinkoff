package pan.artem.tinkoff.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.dto.externalservice.ConditionDto;
import pan.artem.tinkoff.dto.externalservice.CurrentDto;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;
import pan.artem.tinkoff.properties.AppProperties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CurrentWeatherServiceImplTest {

    @Test
    void getCurrentWeather() {
        AppProperties.CurrentWeatherProperties weatherProperties =
                Mockito.mock(AppProperties.CurrentWeatherProperties.class);
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
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
                null
        );
        weatherService = Mockito.spy(weatherService);

        var weatherDto = weatherService.getCurrentWeather("Saint-Petersburg");

        Assertions.assertEquals(weatherDto.temperature(), 10);
        Assertions.assertEquals(weatherDto.weatherType(), "rainy");
        verify(weatherService).getCurrentWeather("Saint-Petersburg");
    }
}