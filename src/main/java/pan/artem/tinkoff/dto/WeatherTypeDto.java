package pan.artem.tinkoff.dto;

import jakarta.validation.constraints.NotEmpty;

public record WeatherTypeDto(@NotEmpty String description) {
}
