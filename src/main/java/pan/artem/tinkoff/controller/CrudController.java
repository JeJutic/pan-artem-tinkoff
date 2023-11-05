package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.service.WeatherCrudService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/weather/{city}")
public class CrudController {

    private final WeatherCrudService weatherService;

    @Operation(
            summary = "Retrieves JSON representation of weather record for specified city",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resource not found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<WeatherFullDto> getWeather(
            @PathVariable("city") String city
    ) {
        var weather = weatherService.getWeather(city);
        return ResponseEntity.ok().body(weather);
    }

    @Operation(
            summary = "Creates a new weather record for specified city",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Weather record created"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resource not found"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> postWeather(
            @PathVariable("city") String city,
            @Valid @RequestBody WeatherFullDto weatherDto
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resource not found"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<?> putWeather(
            @PathVariable("city") String city,
            @Valid @RequestBody WeatherFullDto weatherDto
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
