package pan.artem.tinkoff.repository;

import pan.artem.tinkoff.dto.WeatherFullDto;

import java.util.Optional;

public interface WeatherTypeRepository {

    Optional<WeatherFullDto> getWeather(String city);

    void addWeather(String city, WeatherFullDto weatherFullDto);

    boolean updateWeather(String city, WeatherFullDto weatherFullDto);

    void deleteWeathers(String city);
}
