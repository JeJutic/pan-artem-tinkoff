package pan.artem.tinkoff.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record WeatherDto(@NotNull UUID id, @NotNull Integer temperature, @NotNull Instant dateTime) {
}
