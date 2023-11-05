package pan.artem.tinkoff.repository.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.entity.City;
import pan.artem.tinkoff.entity.Weather;
import pan.artem.tinkoff.entity.WeatherType;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.repository.WeatherRepository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class WeatherRepositoryJPAAdapter implements WeatherRepository {

    private final WeatherRepositoryJPA weatherRepositoryJPA;
    private final WeatherTypeRepositoryJPA weatherTypeRepositoryJPA;
    private final CityRepositoryJPA cityRepositoryJPA;

    private City getCity(String city) {
        var cityEntity = cityRepositoryJPA.getByNameWithWeatherFetching(city);
        if (cityEntity == null) {
            throw new ResourceNotFoundException("No city named " + city + " found");
        }
        return cityEntity;
    }

    private WeatherType getWeatherType(String description) {
        var weatherType = weatherTypeRepositoryJPA.getByDescription(description);
        if (weatherType == null) {
            throw new ResourceNotFoundException(
                    "No weather type with description '" + description + "' found"
            );
        }
        return weatherType;
    }

    @Override
    public Optional<Weather> getWeather(String city, LocalDate date) {
        var cityEntity = getCity(city);
        return cityEntity.getWeathers().stream().filter(
                weather -> weather.getDateTime().toLocalDate().equals(date)
        ).findAny();
    }

    private void addWeather(City city, WeatherFullDto weatherDto, WeatherType weatherType) {
        weatherRepositoryJPA.save(new Weather(
                weatherDto.temperature(),
                weatherDto.dateTime(),
                weatherType,
                city
        ));
    }

    @Override
    public void addWeather(String city, WeatherFullDto weatherDto) {
        var cityEntity = getCity(city);
        var weatherType = getWeatherType(weatherDto.weatherType());
        addWeather(cityEntity, weatherDto, weatherType);
    }

    @Override
    public boolean updateWeather(String city, WeatherFullDto weatherDto) {
        var cityEntity = getCity(city);
        var weatherType = getWeatherType(weatherDto.weatherType());
        for (var weather : cityEntity.getWeathers()) {
            if (weather.getDateTime().toLocalDate()
                    .equals(weatherDto.dateTime().toLocalDate())
            ) {
                weather.setTemperature(weatherDto.temperature());
                weather.setWeatherType(weatherType);
                cityRepositoryJPA.save(cityEntity);
                return true;
            }
        }
        addWeather(cityEntity, weatherDto, weatherType);
        return false;
    }

    @Override
    public void deleteWeathers(String city) {
        try {
            var cityEntity = getCity(city);
            cityEntity.getWeathers().clear();
            cityRepositoryJPA.save(cityEntity);
        } catch (ResourceNotFoundException ignored) {
        }
    }
}
