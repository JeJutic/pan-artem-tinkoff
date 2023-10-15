package pan.artem.tinkoff.service;

import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;

public interface CurrentWeatherClient {

    CurrentWeatherDto getCurrentWeather(String city);
}
