package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import pan.artem.tinkoff.dto.WeatherTypeDto;
import pan.artem.tinkoff.service.WeatherCrudService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/weather-type/{city}")
public class WeatherTypeCrudController {

    private final WeatherCrudService<WeatherTypeDto, WeatherTypeDto> weatherTypeService;

    @Operation(
            summary = "Retrieves JSON representation of weather type record for specified city",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No weather type record for specified city found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<WeatherTypeDto> getWeather(
            @PathVariable("city") String city
    ) {
        WeatherTypeDto weather = weatherTypeService.getWeather(city);
        return ResponseEntity.ok().body(weather);
    }

    @Operation(summary = "Creates a new weather type record for specified city")
    @PostMapping
    public ResponseEntity<?> postWeather(
            @PathVariable("city") String city,
            @Valid @RequestBody WeatherTypeDto weatherDto
    ) {
        weatherTypeService.addWeather(city, weatherDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Updates weather type record with specified city and time or creates it",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Weather record updated"
                    ),
                    @ApiResponse(
                            responseCode = "201",
                            description = "Weather type record created"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<?> putWeather(
            @PathVariable("city") String city,
            @Valid @RequestBody WeatherTypeDto weatherDto
    ) {
        if (weatherTypeService.updateWeather(city, weatherDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Deletes weather type records with specified city")
    @DeleteMapping
    public ResponseEntity<?> deleteWeather(
            @PathVariable("city") String city
    ) {
        weatherTypeService.deleteWeathers(city);
        return ResponseEntity.ok().build();
    }
}
