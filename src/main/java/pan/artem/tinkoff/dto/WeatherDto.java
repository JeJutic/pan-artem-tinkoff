package pan.artem.tinkoff.dto;

import java.time.Instant;
import java.util.UUID;

public record WeatherDto(UUID id, int temperature, Instant dateTime) {
}
