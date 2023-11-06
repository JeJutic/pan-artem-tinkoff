package pan.artem.tinkoff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.service.UserService;
import pan.artem.tinkoff.service.WeatherCrudService;

@AllArgsConstructor
@RestController
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> getWeather(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        userService.registerUser(username, password, "USER");
        return ResponseEntity.ok().build();
    }

}
