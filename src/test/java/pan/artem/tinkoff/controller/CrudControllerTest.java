package pan.artem.tinkoff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import pan.artem.tinkoff.entity.Weather;
import pan.artem.tinkoff.repository.jpa.WeatherRepositoryJPA;
import pan.artem.tinkoff.service.WeatherCrudService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CrudControllerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;
    private ObjectMapper mapper;

    @Autowired
    private WeatherCrudService weatherService;
    @Autowired
    private WeatherRepositoryJPA weatherRepository;

    @Container
    static private H2Container h2 = H2Container.getInstance();

    @DynamicPropertySource
    static void registerH2Properties(DynamicPropertyRegistry registry) {
        h2.registerH2Properties(registry);
    }

    private static final String base = "/api/weather/";
    private static final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

    @PostConstruct
    void init() {
        mapper = springMvcJacksonConverter.getObjectMapper();
    }

    @AfterEach
    void afterEach() {
        weatherRepository.deleteAll();
    }

    private boolean equals(Weather weatherEntity, String city, WeatherFullDto weatherDto) {
        return weatherEntity.getTemperature() == weatherDto.temperature()
                && weatherEntity.getWeatherType().getDescription()
                    .equals(weatherDto.weatherType())
                && weatherEntity.getCity().getName()
                    .equals(city);
    }

    private boolean isPresent(String city, WeatherFullDto weatherDto) {
        logger.debug("weather records count: {}", weatherRepository.findAll().size());
        return weatherRepository.findAll().stream()
                .anyMatch(w -> equals(w, city, weatherDto));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getWeather() throws Exception {
        weatherService.addWeather("Moscow", new WeatherFullDto(
                -10, now, "rainy"
        ));

        mockMvc.perform(get(base + "Moscow"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.temperature").value(-10),
                        jsonPath("$.weatherType").value("rainy")
                );
    }

    @Test
    @WithMockUser(roles = "USER")
    void getWeatherUser() throws Exception {
        getWeather();
    }

    @Test
    void getWeatherUnauthorized() throws Exception {
        weatherService.addWeather("Moscow", new WeatherFullDto(
                -10, now, "rainy"
        ));

        mockMvc.perform(get(base + "Moscow"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postWeather() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now, "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "Moscow")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
        Assertions.assertTrue(isPresent("Moscow", weatherDto));
    }

    @Test
    @WithMockUser(roles = "USER")
    void postWeatherUser() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now, "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "Moscow")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
        Assertions.assertFalse(isPresent("Moscow", weatherDto));
    }

    @Test
    void postWeatherUnauthorized() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now, "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "Moscow")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isUnauthorized());
        Assertions.assertFalse(isPresent("Moscow", weatherDto));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void putWeather() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -11, now, "light rain"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(put(base + "Saint-Petersburg")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is(201));
        Assertions.assertTrue(isPresent("Saint-Petersburg", weatherDto));

        weatherDto = new WeatherFullDto(
                2, now, "sunny"
        );
        json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(put(base + "Saint-Petersburg")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
        Assertions.assertTrue(isPresent("Saint-Petersburg", weatherDto));
    }

    @Test
    @WithMockUser(roles = "USER")
    void putWeatherUser() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -11, now, "light rain"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(put(base + "Saint-Petersburg")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
        Assertions.assertFalse(isPresent("Saint-Petersburg", weatherDto));
    }

    @Test
    void putWeatherUnauthorized() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -11, now, "light rain"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(put(base + "Saint-Petersburg")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isUnauthorized());
        Assertions.assertFalse(isPresent("Saint-Petersburg", weatherDto));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWeather() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, now, "rainy"
        );
        weatherService.addWeather("Moscow", weatherDto);

        mockMvc.perform(delete(base + "Saint-Petersburg"))
                .andExpect(status().isOk());
        mockMvc.perform(delete(base + "moskva"))
                .andExpect(status().isOk());
        Assertions.assertTrue(isPresent("Moscow", weatherDto));

        mockMvc.perform(delete(base + "Moscow"))
                .andExpect(status().isOk());
        Assertions.assertFalse(isPresent("Moscow", weatherDto));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteWeatherUser() throws Exception {
        mockMvc.perform(delete(base + "Saint-Petersburg"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteWeatherUnauthorized() throws Exception {
        mockMvc.perform(delete(base + "Saint-Petersburg"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void yesterdayWeather() throws Exception {
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
        Assertions.assertTrue(isPresent("Yekaterinburg", weatherDto));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unknownCity() throws Exception {
        WeatherFullDto weatherDto = new WeatherFullDto(
                -10, LocalDateTime.now().minusDays(1), "rainy"
        );
        String json = mapper.writeValueAsString(weatherDto);

        mockMvc.perform(post(base + "moskva")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
        Assertions.assertFalse(isPresent("moskva", weatherDto));
    }
}