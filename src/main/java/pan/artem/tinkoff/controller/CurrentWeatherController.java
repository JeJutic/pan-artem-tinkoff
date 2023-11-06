package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pan.artem.tinkoff.dto.WeatherDtoSaveResult;
import pan.artem.tinkoff.service.CurrentWeatherService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/current_weather/{city}")
public class CurrentWeatherController {

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
    public ResponseEntity<WeatherDtoSaveResult> getCurrentWeather(@PathVariable String city) {
        var weather = currentWeatherService.getAndSaveCurrentWeather(city);
        return ResponseEntity.ok(weather);
    }
}
