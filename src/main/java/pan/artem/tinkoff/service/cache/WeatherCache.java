package pan.artem.tinkoff.service.cache;

import pan.artem.tinkoff.dto.WeatherFullDto;

import java.util.Optional;

public interface WeatherCache {

    void save(String city, WeatherFullDto weatherDto);

    Optional<WeatherFullDto> get(String city);

    void invalidate(String city);
}
