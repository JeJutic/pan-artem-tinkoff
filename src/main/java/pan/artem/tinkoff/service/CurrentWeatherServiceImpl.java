package pan.artem.tinkoff.service;

import org.springframework.stereotype.Service;
import pan.artem.tinkoff.dto.WeatherDto;
import pan.artem.tinkoff.dto.externalservice.CurrentWeatherDto;

import java.time.Instant;

@Service
public class CurrentWeatherServiceImpl implements CurrentWeatherService {

    private final CurrentWeatherClient currentWeatherClient;

    public CurrentWeatherServiceImpl(CurrentWeatherClient currentWeatherClient) {
        this.currentWeatherClient = currentWeatherClient;
    }

    @Override
    public WeatherDto getCurrentWeather(String city) {
        CurrentWeatherDto weatherDto = currentWeatherClient.getCurrentWeather(city);
        int temp = weatherDto.current().temperature();
        return new WeatherDto(temp, Instant.now());
    }
}
