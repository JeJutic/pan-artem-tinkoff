package pan.artem.tinkoff.service.cache;

import pan.artem.tinkoff.dto.WeatherFullDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class WeatherCacheImpl implements WeatherCache {

    private final DoublyLinkedQueue<WeatherFullDto> queue = new DoublyLinkedQueue<>();
    private final WeakHashMap<String, DoublyLinkedQueue.Node<WeatherFullDto>> cache
            = new WeakHashMap<>();
    private final int sizeLimit;

    public WeatherCacheImpl(int sizeLimit) {
        if (sizeLimit < 0) {
            throw new IllegalArgumentException();
        }
        this.sizeLimit = sizeLimit;
    }

    @Override
    public synchronized void save(String city, WeatherFullDto weatherDto) {
        var node = queue.add(weatherDto);
        cache.put(city, node);
        if (queue.size() > sizeLimit) {
            queue.remove();
        }
    }

    @Override
    public synchronized Optional<WeatherFullDto> get(String city) {
        var node = cache.get(city);
        if (node == null) {
            return Optional.empty();
        }

        WeatherFullDto weather = node.getValue();
        long minutes = ChronoUnit.MINUTES.between(
                weather.dateTime(),
                LocalDateTime.now(ZoneOffset.UTC)
        );
        if (minutes > 15) {
            return Optional.empty();
        }

        cache.put(city, queue.renew(node));
        return Optional.of(weather);
    }

    @Override
    public synchronized void invalidate(String city) {
        var node = cache.get(city);
        if (node != null) {
            node.remove();
            cache.remove(city);
        }
    }
}
