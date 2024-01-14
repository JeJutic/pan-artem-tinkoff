package pan.artem.tinkoff.service.movingaverage;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pan.artem.tinkoff.properties.AppProperties;
import pan.artem.tinkoff.service.client.CurrentWeatherClient;
import pan.artem.tinkoff.service.movingaverage.event.MovingAverageEvent;

@RequiredArgsConstructor
@Service
public class MovingAverageProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CurrentWeatherClient currentWeatherClient;
    private final AppProperties.MovingAverageProperties properties;
    private final KafkaTemplate<String, MovingAverageEvent> kafkaTemplate;

    private int offset = 0;

    @Scheduled(cron = "*/5 * * * * *")
    public synchronized void run() {
        int index = offset % properties.getCities().size();
        String city = properties.getCities().get(index);

        int temperature = currentWeatherClient.getCurrentWeather(city)
                .current().temperature();
        kafkaTemplate.send(
                properties.getTopicName(),
                new MovingAverageEvent(city, temperature)
        );

        logger.debug("Scheduled task executed");
        offset++;
    }
}
