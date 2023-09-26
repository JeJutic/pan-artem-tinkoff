package pan.artem.tinkoff.dto;

import java.time.Instant;

public record WeatherDto(int temperature, Instant dateTime) {
}
