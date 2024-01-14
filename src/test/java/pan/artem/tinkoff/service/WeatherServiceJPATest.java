package pan.artem.tinkoff.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.entity.City;
import pan.artem.tinkoff.entity.Weather;
import pan.artem.tinkoff.entity.WeatherType;
import pan.artem.tinkoff.repository.jpa.CityRepositoryJPA;
import pan.artem.tinkoff.repository.jpa.WeatherRepositoryJPA;
import pan.artem.tinkoff.repository.jpa.WeatherTypeRepositoryJPA;
import pan.artem.tinkoff.service.cache.WeatherCache;
import pan.artem.tinkoff.service.cache.WeatherCacheImpl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.Mockito.*;

class WeatherServiceJPATest {

    private WeatherRepositoryJPA weatherRepositoryJPA;
    private WeatherTypeRepositoryJPA weatherTypeRepositoryJPA;
    private CityRepositoryJPA cityRepositoryJPA;
    private WeatherCache weatherCache;
    private WeatherServiceJPA weatherService;

    private final City cityEntity = new City(1L, "Moscow",
            List.of(new Weather(
                    10, LocalDateTime.now(ZoneOffset.UTC), new WeatherType(), null
            ))
    );

    @BeforeEach
    void init() {
        weatherRepositoryJPA = Mockito.mock(WeatherRepositoryJPA.class);
        weatherTypeRepositoryJPA = Mockito.mock(WeatherTypeRepositoryJPA.class);
        cityRepositoryJPA = Mockito.mock(CityRepositoryJPA.class);
        weatherCache = Mockito.spy(new WeatherCacheImpl(2));
        weatherService = new WeatherServiceJPA(
                weatherRepositoryJPA,
                weatherTypeRepositoryJPA,
                cityRepositoryJPA,
                weatherCache
        );

    }

    @Test
    void savedInCache() {
        when(cityRepositoryJPA.getByNameWithWeatherFetching("Moscow"))
                .thenReturn(cityEntity);

        var weather = weatherService.getWeather("Moscow");

        Assertions.assertEquals(10, weather.temperature());
        verify(weatherCache).get("Moscow");
        verify(cityRepositoryJPA).getByNameWithWeatherFetching("Moscow");

        Assertions.assertTrue(weatherCache.get("Moscow").isPresent());
    }

    @Test
    void dontGoToDb() {
        weatherCache.save("Moscow", new WeatherFullDto(
                10,
                LocalDateTime.now(ZoneOffset.UTC),
                null
        ));

        var weather = weatherService.getWeather("Moscow");

        Assertions.assertEquals(10, weather.temperature());
        verify(weatherCache).get("Moscow");
        verify(cityRepositoryJPA, never()).getByNameWithWeatherFetching("Moscow");
    }

    @Test
    void invalidateAfterPost() {
        when(cityRepositoryJPA.getByNameWithWeatherFetching("Moscow"))
                .thenReturn(cityEntity);
        weatherCache.save("Moscow", new WeatherFullDto(9, null, null));

        weatherService.addWeather("Moscow", new WeatherFullDto(10, null, "rainy"));
        var weather = weatherService.getWeather("Moscow");

        Assertions.assertEquals(10, weather.temperature());
        verify(weatherCache).get("Moscow");
        verify(cityRepositoryJPA, times(2)).getByNameWithWeatherFetching("Moscow");
    }
}