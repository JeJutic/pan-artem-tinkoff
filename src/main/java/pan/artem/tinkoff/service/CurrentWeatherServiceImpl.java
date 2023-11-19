package pan.artem.tinkoff.service;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.dto.WeatherDtoSaveResult;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.service.cache.WeatherCache;

import java.time.*;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CurrentWeatherServiceImpl implements CurrentWeatherService {

    private final CurrentWeatherClient currentWeatherClient;
    private final WeatherCrudService weatherCrudService;
    @Resource(name = "currentWeatherCache")
    private final WeatherCache weatherCache;

    @Override
    public WeatherFullDto getCurrentWeather(String city) {
        Optional<WeatherFullDto> cached = weatherCache.get(city);
        if (cached.isPresent()) {
            return cached.get();
        }

        CurrentWeatherDto weatherDto = currentWeatherClient.getCurrentWeather(city);
        int temperature = weatherDto.current().temperature();
        String weatherType = weatherDto.current().condition().text();

        var weatherFullDto = new WeatherFullDto(
                temperature,
                LocalDateTime.now(ZoneOffset.UTC),
                weatherType
        );
        weatherCache.save(city, weatherFullDto);
        return weatherFullDto;
    }

    @Override
    public WeatherDtoSaveResult getAndSaveCurrentWeather(String city) {
        var weather = getCurrentWeather(city);
        try {
            weatherCrudService.updateWeather(city, weather);
            return new WeatherDtoSaveResult(weather, null);
        } catch (ResourceNotFoundException e) {
            return new WeatherDtoSaveResult(weather, e.getMessage());
        }
    }
}
