package pan.artem.tinkoff.service;

import pan.artem.tinkoff.dto.WeatherFullDto;

public interface WeatherCrudService {

    WeatherFullDto getWeather(String city);

    void addWeather(String city, WeatherFullDto weatherDto);

    boolean updateWeather(String city, WeatherFullDto weatherDto);

    void deleteWeathers(String city);
}
