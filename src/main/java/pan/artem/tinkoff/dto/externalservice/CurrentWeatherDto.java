package pan.artem.tinkoff.dto.externalservice;

import jakarta.validation.constraints.NotNull;

public record CurrentWeatherDto(@NotNull CurrentDto current) {
}
