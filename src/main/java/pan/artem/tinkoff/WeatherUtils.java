package pan.artem.tinkoff;

import pan.artem.tinkoff.domain.Weather;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class WeatherUtils {

    public static double averageTemperature(List<Weather> weathers) {
        return weathers.stream()
                .map(weather -> (double) weather.getTemperature() / weathers.size())
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public static List<String> findHotRegions(List<Weather> weathers, int temperature) {
        return weathers.stream()
                .filter(weather -> weather.getTemperature() >= temperature)
                .map(Weather::getRegion)
                .distinct()
                .toList();
    }

    public static Map<UUID, List<Integer>> listToIdMap(List<Weather> weathers) {
        return weathers.stream()
                .collect(Collectors.groupingBy(
                        Weather::getId,
                        Collectors.mapping(Weather::getTemperature, Collectors.toList())
                ));
    }

    public static Map<Integer, List<Weather>> listToTemperatureMap(List<Weather> weathers) {
        return weathers.stream()
                .collect(Collectors.groupingBy(
                        Weather::getTemperature
                ));
    }
}
