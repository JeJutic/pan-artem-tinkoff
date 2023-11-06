package pan.artem.tinkoff.dto.externalservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CurrentDto(@JsonProperty("temp_c") @NotNull Integer temperature,
                         @NotNull ConditionDto condition) {
}
