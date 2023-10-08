package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pan.artem.tinkoff.controller.error.ErrorInfo;
import pan.artem.tinkoff.controller.error.ResourceNotFoundException;
import pan.artem.tinkoff.controller.error.externalservice.ExternalServiceInternalErrorException;
import pan.artem.tinkoff.controller.error.externalservice.ExternalServiceLimitExceededException;
import pan.artem.tinkoff.controller.error.RequestNotPermitted;
import pan.artem.tinkoff.dto.WeatherDto;
import pan.artem.tinkoff.service.CurrentWeatherService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/current_weather/{city}")
public class CurrentWeatherController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CurrentWeatherService currentWeatherService;

    @Operation(
            summary = "Retrieves current weather in specified location",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Specified location was not found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<WeatherDto> getCurrentWeather(@PathVariable String city) {
        var weather = currentWeatherService.getCurrentWeather(city);
        return ResponseEntity.ok(weather);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorInfo> handleNotFound(
            HttpServletRequest request, RequestNotPermitted e
    ) {
        logger.info("Rate limiter for current weather service probably exceeded", e);

        return ResponseEntity.status(403).body(
                new ErrorInfo(request.getRequestURL().toString(),
                        "Too many requests for this service")
        );
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

    @ExceptionHandler(ExternalServiceLimitExceededException.class)
    public ResponseEntity<ErrorInfo> handleLimitExceededException(
            HttpServletRequest request,
            ExternalServiceLimitExceededException e
    ) {
        logger.warn("Weather API call limit exceeded", e);

        return ResponseEntity.status(503).body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Service is temporarily unavailable"
                )
        );
    }

    @ExceptionHandler(ExternalServiceInternalErrorException.class)
    public ResponseEntity<ErrorInfo> handleInternalErrorException(HttpServletRequest request) {
        return ResponseEntity.status(503).body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Service is temporarily unavailable"
                )
        );
    }
}