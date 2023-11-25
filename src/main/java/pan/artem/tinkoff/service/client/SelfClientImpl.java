package pan.artem.tinkoff.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pan.artem.tinkoff.dto.WeatherDtoSaveResult;

@Service
public class SelfClientImpl implements SelfClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RestTemplate restTemplate;

    public SelfClientImpl(
            @Qualifier("selfRestTemplate") RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    @Override
    public int getTemperature(String city) {
        WeatherDtoSaveResult weatherDtoSaveResult = restTemplate.getForObject(
                "/current_weather/{city}",
                WeatherDtoSaveResult.class,
                city
        );
        logger.debug("Weather DTO received: {}", weatherDtoSaveResult);

        return weatherDtoSaveResult.weatherDto().temperature();
    }
}
