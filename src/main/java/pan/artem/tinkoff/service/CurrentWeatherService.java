package pan.artem.tinkoff.service;

import pan.artem.tinkoff.dto.WeatherDto;

public interface CurrentWeatherService {

    WeatherDto getCurrentWeather(String city);
}
