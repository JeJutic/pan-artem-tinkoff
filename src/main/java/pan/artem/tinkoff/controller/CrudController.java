package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import pan.artem.tinkoff.domain.Weather;
import pan.artem.tinkoff.dto.WeatherDto;

import java.time.*;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api/wheather/{city}")
public class CrudController {

    ConcurrentLinkedQueue<Weather> data = new ConcurrentLinkedQueue<>();

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
    private ResponseEntity<Weather> getWeather(
            @PathVariable("city") String city
    ) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        var optionalWeather = data.stream()
                .filter(weather ->
                {
                    LocalDate theDay = ZonedDateTime
                            .ofInstant(weather.getDateTime(), ZoneOffset.UTC)
                            .toLocalDate();
                    return Objects.equals(weather.getRegion(), city) && today.equals(theDay);
                })
                .findAny();
        return ResponseEntity
                .of(optionalWeather);
    }

    @Operation(summary = "Creates a new weather record for specified city")
    @PostMapping
    private ResponseEntity<?> postWeather(
            @PathVariable("city") String city,
            @RequestBody WeatherDto weatherDto
    ) {
        data.add(new Weather(city, weatherDto.temperature(), weatherDto.dateTime()));
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
    private ResponseEntity<?> putWeather(
            @PathVariable("city") String city,
            @RequestBody WeatherDto weatherDto
    ) {
        for (var weather : data) {
            if (weather.getRegion().equals(city) &&
                    weather.getDateTime().equals(weatherDto.dateTime())) {
                weather.setTemperature(weatherDto.temperature());
                return ResponseEntity.ok().build();
            }
        }

        data.add(new Weather(city, weatherDto.temperature(), weatherDto.dateTime()));
        return ResponseEntity
                .status(201)
                .build();
    }

    @Operation(summary = "Deletes weather records with specified city")
    @DeleteMapping
    private ResponseEntity<?> deleteWeather(
            @PathVariable("city") String city
    ) {
        data.removeIf(weather -> Objects.equals(weather.getRegion(), city));
        return ResponseEntity.ok().build();
    }
}
