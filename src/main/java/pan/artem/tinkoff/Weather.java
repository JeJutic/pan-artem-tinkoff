package pan.artem.tinkoff;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor
public class Weather {
    UUID id;
    String region;
    int temperature;
    Instant dateTime;

    public Weather(String region, int temperature) {
        this(UUID.randomUUID(), region, temperature, Instant.now());
    }
}
