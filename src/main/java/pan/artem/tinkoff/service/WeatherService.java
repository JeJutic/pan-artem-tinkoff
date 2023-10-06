package pan.artem.tinkoff.service;

import pan.artem.tinkoff.domain.Weather;
import pan.artem.tinkoff.dto.WeatherDto;

public interface WeatherService {

    Weather getWeather(String city);

    void addWeather(String city, WeatherDto weatherDto);

    boolean updateWeather(String city, WeatherDto weatherDto);

    void deleteWeathers(String city);
}
