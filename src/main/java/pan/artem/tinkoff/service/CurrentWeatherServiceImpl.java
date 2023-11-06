package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.dto.WeatherDtoSaveResult;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;
import pan.artem.tinkoff.exception.ResourceNotFoundException;

import java.time.*;

@AllArgsConstructor
@Service
public class CurrentWeatherServiceImpl implements CurrentWeatherService {

    private final CurrentWeatherClient currentWeatherClient;
    private final WeatherCrudService weatherCrudService;

    @Override
    public WeatherFullDto getCurrentWeather(String city) {
        CurrentWeatherDto weatherDto = currentWeatherClient.getCurrentWeather(city);
        int temperature = weatherDto.current().temperature();
        String weatherType = weatherDto.current().condition().text();

        return new WeatherFullDto(
                temperature,
                LocalDateTime.now(ZoneOffset.UTC),
                weatherType
        );
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
