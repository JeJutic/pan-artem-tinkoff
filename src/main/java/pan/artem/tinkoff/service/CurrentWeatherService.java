package pan.artem.tinkoff.service;

import pan.artem.tinkoff.dto.WeatherDtoSaveResult;
import pan.artem.tinkoff.dto.WeatherFullDto;

public interface CurrentWeatherService {

    WeatherFullDto getCurrentWeather(String city);

    WeatherDtoSaveResult getAndSaveCurrentWeather(String city);
}
