package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.domain.Weather;
import pan.artem.tinkoff.dto.WeatherDto;
import pan.artem.tinkoff.repository.WeatherRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@AllArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    @Override
    public Optional<Weather> getWeather(String city) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        return weatherRepository.getWeather(city, today);
    }

    @Override
    public void addWeather(String city, WeatherDto weatherDto) {
        weatherRepository.addWeather(city, weatherDto);
    }

    @Override
    public boolean updateWeather(String city, WeatherDto weatherDto) {
        return weatherRepository.updateWeather(city, weatherDto);
    }

    @Override
    public void deleteWeathers(String city) {
        weatherRepository.deleteWeathers(city);
    }
}
