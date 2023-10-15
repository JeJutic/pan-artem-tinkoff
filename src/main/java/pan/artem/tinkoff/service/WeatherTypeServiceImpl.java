package pan.artem.tinkoff.service;

import org.springframework.stereotype.Service;
import pan.artem.tinkoff.dto.WeatherTypeDto;

@Service
public class WeatherTypeServiceImpl implements WeatherCrudService<WeatherTypeDto, WeatherTypeDto> {


    @Override
    public WeatherTypeDto getWeather(String city) {
        return null;
    }

    @Override
    public void addWeather(String city, WeatherTypeDto weatherDto) {

    }

    @Override
    public boolean updateWeather(String city, WeatherTypeDto weatherDto) {
        return false;
    }

    @Override
    public void deleteWeathers(String city) {

    }
}
