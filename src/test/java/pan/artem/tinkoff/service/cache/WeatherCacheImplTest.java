package pan.artem.tinkoff.service.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pan.artem.tinkoff.dto.WeatherFullDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

class WeatherCacheImplTest {

    private final WeatherFullDto weatherDto1
            = new WeatherFullDto(1, LocalDateTime.now(ZoneOffset.UTC), null);
    private final WeatherFullDto weatherDto2
            = new WeatherFullDto(2, LocalDateTime.now(ZoneOffset.UTC), null);

    @Test
    void save() {
        WeatherCache weatherCache = new WeatherCacheImpl(2);
        weatherCache.save("one", weatherDto1);
        weatherCache.save("two", weatherDto2);

        var optional = weatherCache.get("one");
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(1, optional.get().temperature());

        optional = weatherCache.get("two");
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(2, optional.get().temperature());

        optional = weatherCache.get("three");
        Assertions.assertTrue(optional.isEmpty());
    }

    @Test
    void invalidate() {
        WeatherCache weatherCache = new WeatherCacheImpl(2);
        weatherCache.save("one", weatherDto1);
        weatherCache.invalidate("one");

        var optional = weatherCache.get("one");
        Assertions.assertTrue(optional.isEmpty());
    }

    @Test
    void invalidateNothing() {
        WeatherCache weatherCache = new WeatherCacheImpl(2);
        weatherCache.save("one", weatherDto1);

        weatherCache.invalidate("two");
    }

    @Test
    void invalidateAndReinsert() {
        WeatherCache weatherCache = new WeatherCacheImpl(2);
        weatherCache.save("one", weatherDto1);
        weatherCache.invalidate("one");
        weatherCache.save("one", weatherDto2);

        var optional = weatherCache.get("one");
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(2, optional.get().temperature());
    }
}