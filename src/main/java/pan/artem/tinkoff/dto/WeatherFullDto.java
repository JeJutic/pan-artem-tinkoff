package pan.artem.tinkoff.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.*;

public record WeatherFullDto(@NotNull Integer temperature,
                             @NotNull LocalDateTime dateTime,
                             @NotEmpty String weatherType) {
}
