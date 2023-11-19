package pan.artem.tinkoff.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "app.cache.course")
    public static class CacheProperties {

        @NotNull
        @Size
        private Integer size;
    }

}
