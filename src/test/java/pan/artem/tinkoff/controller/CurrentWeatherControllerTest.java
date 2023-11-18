package pan.artem.tinkoff.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CurrentWeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String base = "/api/current_weather/";

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCurrentWeather() throws Exception {
        mockMvc.perform(get(base + "Moscow"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getCurrentWeatherUser() throws Exception {
        getCurrentWeather();
    }

    @Test
    void getCurrentWeatherUnauthorized() throws Exception {
        mockMvc.perform(get(base + "Moscow"))
                .andExpect(status().isUnauthorized());
    }
}