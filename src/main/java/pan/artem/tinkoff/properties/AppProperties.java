package pan.artem.tinkoff.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @Getter
    @Setter
    @Validated
    @ConfigurationProperties(prefix = "app.current-weather-service")
    public static class CurrentWeatherProperties {

        @NotEmpty
        private String baseUrl;

        @NotEmpty
        private String apiToken;
    }

}
