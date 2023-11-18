package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.entity.City;
import pan.artem.tinkoff.entity.Weather;
import pan.artem.tinkoff.entity.WeatherType;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.repository.jpa.CityRepositoryJPA;
import pan.artem.tinkoff.repository.jpa.WeatherRepositoryJPA;
import pan.artem.tinkoff.repository.jpa.WeatherTypeRepositoryJPA;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Primary
public class WeatherServiceJPA implements WeatherCrudService {

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
            weatherType = new WeatherType();
            weatherType.setDescription(description);
            weatherTypeRepositoryJPA.save(weatherType);
        }
        return weatherType;
    }

    private Optional<Weather> getWeather(String city, LocalDate date) {
        var cityEntity = getCity(city);
        return cityEntity.getWeathers().stream().filter(
                weather -> weather.getDateTime().toLocalDate().equals(date)
        ).findAny();
    }

    @Override
    public WeatherFullDto getWeather(String city) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        var optional = getWeather(city, today);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Weather record found for the current date in " + city
            );
        }
        Weather weather = optional.get();
        return new WeatherFullDto(
                weather.getTemperature(),
                weather.getDateTime(),
                weather.getWeatherType().getDescription()
        );
    }

    private void addWeather(City city, WeatherFullDto weatherDto, WeatherType weatherType) {
        weatherRepositoryJPA.save(new Weather(
                weatherDto.temperature(),
                weatherDto.dateTime(),
                weatherType,
                city
        ));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void addWeather(String city, WeatherFullDto weatherDto) {
        var cityEntity = getCity(city);
        var weatherType = getWeatherType(weatherDto.weatherType());
        addWeather(cityEntity, weatherDto, weatherType);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
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
