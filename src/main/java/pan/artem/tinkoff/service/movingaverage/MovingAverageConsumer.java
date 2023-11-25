package pan.artem.tinkoff.service.movingaverage;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pan.artem.tinkoff.entity.MovingAverageRecord;
import pan.artem.tinkoff.repository.MovingAverage;
import pan.artem.tinkoff.service.movingaverage.event.MovingAverageEvent;

@AllArgsConstructor
@Service
public class MovingAverageConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovingAverage movingAverage;

    @Transactional
    @KafkaListener(topics = "moving_average")
    public void consume(MovingAverageEvent event) {
        logger.debug("Moving average event received: {}", event);

        var newRecord = new MovingAverageRecord();
        newRecord.setTemperature(event.getTemperature());
        movingAverage.save(newRecord);

        long count = movingAverage.count();
        if (count < 30) {
            return;
        } else if (count > 30) {
            movingAverage.deleteLatest();
        }
        double average = movingAverage.findAll().stream()
                .mapToInt(MovingAverageRecord::getTemperature)
                .average().getAsDouble();
        logger.info("New moving average of last 30 records found: {}", average);
    }
}
