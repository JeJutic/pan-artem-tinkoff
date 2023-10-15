package pan.artem.tinkoff.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.dto.WeatherTypeDto;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.repository.WeatherTypeRepository;

@AllArgsConstructor
@Service
public class WeatherTypeServiceImpl implements WeatherCrudService<WeatherTypeDto, WeatherTypeDto> {

    private final WeatherTypeRepository weatherTypeRepository;

    @Override
    public WeatherTypeDto getWeather(String city) {
        var optional = weatherTypeRepository.getWeather(city);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Weather type record found for " + city
            );
        }
        return optional.get();
    }

    @Override
    public void addWeather(String city, WeatherTypeDto weatherTypeDto) {
        weatherTypeRepository.addWeather(city, weatherTypeDto);
    }

    @Override
    public boolean updateWeather(String city, WeatherTypeDto weatherTypeDto) {
        return weatherTypeRepository.updateWeather(city, weatherTypeDto);
    }

    @Override
    public void deleteWeathers(String city) {
        weatherTypeRepository.deleteWeathers(city);
    }
}
