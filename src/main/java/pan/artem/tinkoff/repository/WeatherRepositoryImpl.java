package pan.artem.tinkoff.repository;

import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.domain.Weather;
import pan.artem.tinkoff.dto.WeatherDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class WeatherRepositoryImpl implements WeatherRepository {

    ConcurrentHashMap<String, UUID> cityToId = new ConcurrentHashMap<>();
    ConcurrentLinkedQueue<Weather> data = new ConcurrentLinkedQueue<>();

    boolean isSameDate(LocalDate today, Instant other) {
        return ZonedDateTime
                .ofInstant(other, ZoneOffset.UTC)
                .toLocalDate()
                .equals(today);
    }

    @Override
    public UUID getCityId(String city) {
        cityToId.putIfAbsent(city, UUID.randomUUID());
        return cityToId.get(city);
    }

    @Override
    public Optional<Weather> getWeather(String city, LocalDate date) {
        return data.stream()
                .filter(weather -> weather.getRegion().equals(city)
                        && isSameDate(date, weather.getDateTime()))
                .findAny();
    }

    @Override
    public void addWeather(String city, WeatherDto weatherDto) {
        data.add(new Weather(
                        getCityId(city),
                        city,
                        weatherDto.temperature(),
                        weatherDto.dateTime()
                )
        );
    }

    @Override
    public boolean updateWeather(String city, WeatherDto weatherDto) {
        var id = getCityId(city);

        for (var weather : data) {
            if (weather.getId().equals(id) &&
                    weather.getDateTime().equals(weatherDto.dateTime())) {
                weather.setTemperature(weatherDto.temperature());
                return true;
            }
        }

        data.add(new Weather(id, city, weatherDto.temperature(), weatherDto.dateTime()));
        return false;
    }

    @Override
    public void deleteWeathers(String city) {
        data.removeIf(weather -> Objects.equals(weather.getRegion(), city));
    }
}
