package pan.artem.tinkoff.repository;

import pan.artem.tinkoff.domain.Weather;
import pan.artem.tinkoff.dto.WeatherDto;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface WeatherRepository {

    UUID getCityId(String city);

    Optional<Weather> getWeather(String city, LocalDate date);

    void addWeather(String city, WeatherDto weatherDto);

    boolean updateWeather(String city, WeatherDto weatherDto);

    void deleteWeathers(String city);
}
