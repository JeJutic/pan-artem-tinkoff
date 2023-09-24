package pan.artem.tinkoff;

import java.time.Instant;
import java.util.UUID;

public class Weather {
    private final UUID id;
    private final String region;
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

    public UUID getId() {
        return id;
    }

    public int getTemperature() {
        return temperature;
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
