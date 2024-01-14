package pan.artem.tinkoff.service.client;

import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;

public interface CurrentWeatherClient {

    CurrentWeatherDto getCurrentWeather(String city);
}
