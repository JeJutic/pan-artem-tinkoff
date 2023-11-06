package pan.artem.tinkoff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pan.artem.tinkoff.H2Container;
import pan.artem.tinkoff.dto.WeatherFullDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CrudControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;
    @Container
    static private H2Container h2 = H2Container.getInstance();

    @DynamicPropertySource
    static void registerH2Properties(DynamicPropertyRegistry registry) {
        h2.registerH2Properties(registry);
    }

    private static final String base = "/api/weather/";
    private static final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

    @Test
    @WithMockUser(roles = "ADMIN")
    void happyPath() throws Exception {
        ObjectMapper mapper = springMvcJacksonConverter.getObjectMapper();
        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now, "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "Moscow")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
        mockMvc.perform(get(base + "Moscow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(-10))
                .andExpect(jsonPath("$.weatherType").value("rainy"));

        weatherDto = new WeatherFullDto(
                -11, now, "light rain"
        );
        json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(put(base + "Saint-Petersburg")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is(201));

        mockMvc.perform(get(base + "Saint-Petersburg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(-11))
                .andExpect(jsonPath("$.weatherType").value("light rain"));

        weatherDto = new WeatherFullDto(
                1, now, "rainy"
        );
        json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(put(base + "Saint-Petersburg")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is(200));
        mockMvc.perform(get(base + "Saint-Petersburg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(1))
                .andExpect(jsonPath("$.weatherType").value("rainy"));

        mockMvc.perform(delete(base + "Saint-Petersburg"))
                .andExpect(status().isOk());
        mockMvc.perform(delete(base + "moskva"))
                .andExpect(status().isOk());
        mockMvc.perform(get(base + "Moscow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(-10))
                .andExpect(jsonPath("$.weatherType").value("rainy"));
        mockMvc.perform(delete(base + "Moscow"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void yesterdayWeather() throws Exception {
        ObjectMapper mapper = springMvcJacksonConverter.getObjectMapper();

        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now.minusDays(1), "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "Yekaterinburg")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
        mockMvc.perform(get(base + "Yekaterinburg"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unknownCity() throws Exception {
        ObjectMapper mapper = springMvcJacksonConverter.getObjectMapper();

        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, LocalDateTime.now().minusDays(1), "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "moskva")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void addWeather() throws Exception {
        ObjectMapper mapper = springMvcJacksonConverter.getObjectMapper();

        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now, "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "Moscow").with(user("user"))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
        mockMvc.perform(post(base + "Moscow").with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
        mockMvc.perform(get(base + "Moscow").with(user("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get(base + "Moscow").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void putWeather() throws Exception {
        ObjectMapper mapper = springMvcJacksonConverter.getObjectMapper();

        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now, "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(put(base + "Moscow").with(user("user"))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
        mockMvc.perform(put(base + "Moscow").with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get(base + "Moscow").with(user("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get(base + "Moscow").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteWeather() throws Exception {
        mockMvc.perform(delete(base + "Moscow").with(user("user")))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete(base + "Moscow").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        mockMvc.perform(get(base + "Moscow").with(user("user")))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(base + "Moscow").with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }
}