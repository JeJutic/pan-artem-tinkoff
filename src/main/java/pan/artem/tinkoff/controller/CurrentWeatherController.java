package pan.artem.tinkoff.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pan.artem.tinkoff.controller.error.ErrorInfo;
import pan.artem.tinkoff.controller.error.ResourceNotFoundException;
import pan.artem.tinkoff.controller.error.weatherapi.WeatherApiInternalErrorException;
import pan.artem.tinkoff.controller.error.weatherapi.WeatherApiLimitExceededException;
import pan.artem.tinkoff.dto.WeatherDto;
import pan.artem.tinkoff.service.CurrentWeatherService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/current_weather/{city}")
public class CurrentWeatherController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CurrentWeatherService currentWeatherService;

    @GetMapping
    public ResponseEntity<WeatherDto> getCurrentWeather(@PathVariable String city) {
        var weather = currentWeatherService.getCurrentWeather(city);
        return ResponseEntity.ok(weather);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleNotFound(
            HttpServletRequest request, ResourceNotFoundException e
    ) {
        return ResponseEntity.status(404).body(
                new ErrorInfo(request.getRequestURL().toString(),
                        "Specified location was not found: " + e.getMessage())
        );
    }

    @ExceptionHandler(WeatherApiLimitExceededException.class)
    public ResponseEntity<ErrorInfo> handleLimitExceededException(
            HttpServletRequest request,
            WeatherApiLimitExceededException e
    ) {
        logger.warn("Weather API call limit exceeded", e);

        return ResponseEntity.status(503).body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Service is temporarily unavailable"
                )
        );
    }

    @ExceptionHandler(WeatherApiInternalErrorException.class)
    public ResponseEntity<ErrorInfo> handleInternalErrorException(HttpServletRequest request) {
        return ResponseEntity.status(503).body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Service is temporarily unavailable"
                )
        );
    }
}
