package pan.artem.tinkoff.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pan.artem.tinkoff.service.UserService;

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
