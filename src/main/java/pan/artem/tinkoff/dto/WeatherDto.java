package pan.artem.tinkoff.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record WeatherDto(@NotNull Integer temperature, @NotNull Instant dateTime) {
}
