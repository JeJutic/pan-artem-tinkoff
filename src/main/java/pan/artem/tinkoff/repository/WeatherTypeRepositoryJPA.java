package pan.artem.tinkoff.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.dto.WeatherTypeDto;
import pan.artem.tinkoff.entity.CityEntity;
import pan.artem.tinkoff.entity.WeatherTypeEntity;
import pan.artem.tinkoff.exception.ResourceNotFoundException;

import java.util.Optional;

@AllArgsConstructor
@Repository
@Primary
public class WeatherTypeRepositoryJPA implements WeatherTypeRepository {

    private final CityEntityRepositoryJPA cityEntityRepository;
    private final WeatherTypeEntityRepository weatherTypeEntityRepository;

    @Override
    public Optional<WeatherTypeDto> getWeather(String city) {
        CityEntity cityEntity = getCityEntity(city);
        WeatherTypeEntity weatherTypeEntity = cityEntity.getWeatherTypeEntity();
        if (weatherTypeEntity == null) {
            return Optional.empty();
        }
        return Optional.of(new WeatherTypeDto(weatherTypeEntity.getDescription()));
    }

    private CityEntity getCityEntity(String city) {
        CityEntity cityEntity = cityEntityRepository.getByName(city);
        if (cityEntity == null) {
            throw new ResourceNotFoundException("No city named " + city + " found");
        }
        return cityEntity;
    }

    private WeatherTypeEntity getWeatherTypeEntity(WeatherTypeDto weatherTypeDto) {
        WeatherTypeEntity weatherTypeEntity = weatherTypeEntityRepository
                .getByDescription(weatherTypeDto.description());
        if (weatherTypeEntity == null) {
            throw new ResourceNotFoundException(
                    "No weather type characterized as \"" +
                            weatherTypeDto.description() +
                            "\" found"
            );
        }
        return weatherTypeEntity;
    }

    private void addWeather(CityEntity cityEntity, WeatherTypeEntity weatherTypeEntity) {
        cityEntity.setWeatherTypeEntity(weatherTypeEntity);
        cityEntityRepository.save(cityEntity);
    }

    @Override
    public void addWeather(String city, WeatherTypeDto weatherTypeDto) {
        CityEntity cityEntity = getCityEntity(city);
        WeatherTypeEntity weatherTypeEntity = getWeatherTypeEntity(weatherTypeDto);
        addWeather(cityEntity, weatherTypeEntity);
    }

    @Override
    public boolean updateWeather(String city, WeatherTypeDto weatherTypeDto) {
        CityEntity cityEntity = getCityEntity(city);
        WeatherTypeEntity weatherTypeEntity = getWeatherTypeEntity(weatherTypeDto);
        if (cityEntity.getWeatherTypeEntity() == null) {
            addWeather(cityEntity, weatherTypeEntity);
            return false;
        }
        cityEntity.setWeatherTypeEntity(weatherTypeEntity);
        cityEntityRepository.save(cityEntity);
        return true;
    }

    @Override
    public void deleteWeathers(String city) {
        CityEntity cityEntity = getCityEntity(city);
        cityEntity.setWeatherTypeEntity(null);
        cityEntityRepository.save(cityEntity);
    }
}
