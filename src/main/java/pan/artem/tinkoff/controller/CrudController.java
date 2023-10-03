package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import pan.artem.tinkoff.controller.error.ErrorInfo;
import pan.artem.tinkoff.controller.error.GlobalExceptionHandler;
import pan.artem.tinkoff.controller.error.ResourceNotFoundException;
import pan.artem.tinkoff.domain.Weather;
import pan.artem.tinkoff.dto.WeatherDto;
import pan.artem.tinkoff.service.WeatherService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/weather/{city}")
public class CrudController {

    private final WeatherService weatherService;

    @Operation(
            summary = "Retrieves JSON representation of weather record for specified city",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No weather record for specified city found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Weather> getWeather(
            @PathVariable("city") String city
    ) {
        var optionalWeather = weatherService.getWeather(city);
        if (optionalWeather.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Weather record found for the current date in " + city
            );
        }
        return ResponseEntity.ok().body(optionalWeather.get());
    }

    @Operation(summary = "Creates a new weather record for specified city")
    @PostMapping
    public ResponseEntity<?> postWeather(
            @PathVariable("city") String city,
            @Valid @RequestBody WeatherDto weatherDto
    ) {
        weatherService.addWeather(city, weatherDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Updates weather record with specified city and time or creates it",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Weather record updated"
                    ),
                    @ApiResponse(
                            responseCode = "201",
                            description = "Weather record created"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<?> putWeather(
            @PathVariable("city") String city,
            @Valid @RequestBody WeatherDto weatherDto
    ) {
        if (weatherService.updateWeather(city, weatherDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Deletes weather records with specified city")
    @DeleteMapping
    public ResponseEntity<?> deleteWeather(
            @PathVariable("city") String city
    ) {
        weatherService.deleteWeathers(city);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleNotFound(
            HttpServletRequest request, ResourceNotFoundException e
    ) {
        return ResponseEntity.status(404).body(
                new ErrorInfo(request.getRequestURL().toString(),
                        "Weather record not found: " + e.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleValidationException(
            HttpServletRequest request, MethodArgumentNotValidException e
    ) {
        return ResponseEntity.badRequest().body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Data format for Weather record is violated: " +
                                GlobalExceptionHandler.methodArgumentNotValidString(e)
                )
        );
    }
}
