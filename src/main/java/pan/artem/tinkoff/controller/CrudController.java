package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
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
        Weather weather = weatherService.getWeather(city);
        return ResponseEntity.ok().body(weather);
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
}
