package pan.artem.tinkoff.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
    @Validated
    @ConfigurationProperties(prefix = "app.cache.course")
    public static class CacheProperties {

        @NotNull
        @Min(0)
        private Integer size;
    }

    @Getter
    @Setter
    @Validated
    @ConfigurationProperties(prefix = "app.moving-average")
    public static class MovingAverageProperties {

        @NotEmpty
        private String topicName;

        @NotNull
        @Size(min = 1, max = 5)
        private List<String> cities;

        @NotNull
        @Size(min = 8)
        private String cronPassword;
    }

}
