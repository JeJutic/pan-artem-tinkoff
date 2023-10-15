package pan.artem.tinkoff.repository;

import pan.artem.tinkoff.dto.WeatherTypeDto;

import java.util.Optional;

public interface WeatherTypeRepository {

    Optional<WeatherTypeDto> getWeather(String city);

    void addWeather(String city, WeatherTypeDto weatherTypeDto);

    boolean updateWeather(String city, WeatherTypeDto weatherTypeDto);

    void deleteWeathers(String city);
}
