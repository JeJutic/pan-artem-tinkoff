package pan.artem.tinkoff.repository;

import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.entity.Weather;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository {

    Optional<Weather> getWeather(String city, LocalDate date);

    void addWeather(String city, WeatherFullDto weatherDto);

    boolean updateWeather(String city, WeatherFullDto weatherDto);

    void deleteWeathers(String city);
}
