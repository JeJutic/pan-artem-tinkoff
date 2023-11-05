package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.entity.Weather;
import pan.artem.tinkoff.repository.WeatherRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;

@AllArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherCrudService {

    private final WeatherRepository weatherRepository;

    @Override
    public WeatherFullDto getWeather(String city) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        var optional = weatherRepository.getWeather(city, today);
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

    @Override
    public void addWeather(String city, WeatherFullDto weatherDto) {
        weatherRepository.addWeather(city, weatherDto);
    }

    @Override
    public boolean updateWeather(String city, WeatherFullDto weatherDto) {
        return weatherRepository.updateWeather(city, weatherDto);
    }

    @Override
    public void deleteWeathers(String city) {
        weatherRepository.deleteWeathers(city);
    }
}
