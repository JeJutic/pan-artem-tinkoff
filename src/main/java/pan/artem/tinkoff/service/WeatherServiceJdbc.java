package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.repository.jdbc.WeatherRepositoryJdbc;

import java.time.LocalDate;
import java.time.ZoneOffset;

@AllArgsConstructor
@Service
@Primary
public class WeatherServiceJdbc implements WeatherCrudService {

    private final WeatherRepositoryJdbc weatherRepository;

    @Override
    public WeatherFullDto getWeather(String city) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        var optional = weatherRepository.getWeather(city, today);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Weather record found for the current date in " + city
            );
        }
        return optional.get().weatherDto();
    }

    @Override
    public void addWeather(String city, WeatherFullDto weatherDto) {
        weatherRepository.addWeather(city, weatherDto);
    }

    @Override
    public boolean updateWeather(String city, WeatherFullDto weatherDto) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        return weatherRepository.updateWeather(city, today, weatherDto);
    }

    @Override
    public void deleteWeathers(String city) {
        weatherRepository.deleteWeathers(city);
    }
}
