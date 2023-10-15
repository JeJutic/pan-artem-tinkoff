package pan.artem.tinkoff.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.dto.WeatherTypeDto;
import pan.artem.tinkoff.exception.ResourceNotFoundException;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class WeatherTypeRepositoryJdbc implements WeatherTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<WeatherTypeDto> getWeather(String city) {
        WeatherTypeDto weatherTypeDto = jdbcTemplate.queryForObject(
            "SELECT description " +
                    "FROM city LEFT OUTER JOIN weather ON city.id = weather.city_id " +
                    "LEFT OUTER JOIN weather_type ON weather_type.id = weather.weather_type_id " +
                    "WHERE city.name = ? LIMIT 1",
                WeatherTypeDto.class,
                city
        );
        return Optional.ofNullable(weatherTypeDto);
    }

    private int getCityId(String city) {
        Integer value = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SELECT id FROM city WHERE name = ? LIMIT 1)",
                Integer.class,
                city
        );
        if (value == null) {
            throw new ResourceNotFoundException("No city named " + city + " found");
        }
        return value;
    }

    private int getWeatherTypeId(WeatherTypeDto weatherTypeDto) {
        Integer value = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SELECT id FROM weather_type WHERE description = ? LIMIT 1)",
                Integer.class,
                weatherTypeDto.description()
        );
        if (value == null) {
            throw new ResourceNotFoundException(
                    "No weather type characterized as \"" +
                            weatherTypeDto.description() +
                            "\" found"
            );
        }
        return value;
    }

    private void addWeather(int cityId, int weatherTypeId) {
        jdbcTemplate.update(
                "INSERT INTO weather (city_id, weather_type_id) VALUES (?, ?)",
                cityId,
                weatherTypeId
        );
    }

    @Override
    public void addWeather(String city, WeatherTypeDto weatherTypeDto) {
        int cityId = getCityId(city);
        int weatherTypeId = getWeatherTypeId(weatherTypeDto);
        addWeather(cityId, weatherTypeId);
    }

    @Override
    public boolean updateWeather(String city, WeatherTypeDto weatherTypeDto) {
        int cityId = getCityId(city);
        int weatherTypeId = getWeatherTypeId(weatherTypeDto);
        int rowsAffected = jdbcTemplate.update(
                "UPDATE weather SET weather_type_id = ? WHERE city_id = ?",
                weatherTypeId,
                cityId
        );
        if (rowsAffected == 0) {
            addWeather(cityId, weatherTypeId);
            return false;
        }
        return true;
    }

    @Override
    public void deleteWeathers(String city) {
        int cityId = getCityId(city);
        jdbcTemplate.update(
                "DELETE FROM weather WHERE city_id = ?",
                cityId
        );
    }
}
