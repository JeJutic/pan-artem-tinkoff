package pan.artem.tinkoff.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Weather {
    UUID id;
    String region;
    int temperature;
    Instant dateTime;

    public Weather(String region, int temperature, Instant dateTime) {
        this(UUID.randomUUID(), region, temperature, dateTime);
    }

    public Weather(String region, int temperature) {
        this(region, temperature, Instant.now());
    }
}
