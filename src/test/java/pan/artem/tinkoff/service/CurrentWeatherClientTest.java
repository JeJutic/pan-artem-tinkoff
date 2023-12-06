package pan.artem.tinkoff.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pan.artem.tinkoff.service.client.CurrentWeatherClient;

@SpringBootTest
class CurrentWeatherClientTest {

    @Autowired
    private CurrentWeatherClient currentWeatherClient;

    @Test
    void getCurrentWeather() {
        var dto = currentWeatherClient.getCurrentWeather("Moscow");

        Assertions.assertNotNull(dto.current().temperature());
    }
}