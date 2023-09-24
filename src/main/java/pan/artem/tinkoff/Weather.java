package pan.artem.tinkoff;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public class Weather {
    @Getter
    private final UUID id;
    private final String region;
    @Getter
    private final int temperature;
    private final Instant dateTime;

    public Weather(UUID id, String region, int temperature, Instant dateTime) {
        this.id = id;
        this.region = region;
        this.temperature = temperature;
        this.dateTime = dateTime;
    }

    public Weather(String region, int temperature) {
        this(UUID.randomUUID(), region, temperature, Instant.now());
    }

    @Override
    public String toString() {
        return "Weather{" +
                "region='" + region + '\'' +
                ", temperature=" + temperature +
                ", dateTime=" + dateTime +
                '}';
    }
}
