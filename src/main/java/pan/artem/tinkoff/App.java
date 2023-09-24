package pan.artem.tinkoff;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class App {

    public static double averageTemperature(List<Weather> weathers) {
        if (weathers.isEmpty()) {
            return 0;
        }
        return weathers.stream()
                .map(weather -> (double) weather.getTemperature() / weathers.size())
                .reduce(Double::sum)
                .get();
    }

    public static List<Weather> findHotRegions(List<Weather> weathers, int temperature) {
        return weathers.stream()
                .filter(weather -> weather.getTemperature() >= temperature)
                .toList();
    }

    public static Map<UUID, Integer> listToIdMap(List<Weather> weathers) {
        return weathers.stream()
                .collect(Collectors.toMap(
                        Weather::getId,
                        Weather::getTemperature
                ));
    }

    public static Map<Integer, List<Weather>> listToTemperatureMap(List<Weather> weathers) {
        return weathers.stream()
                .collect(Collectors.groupingBy(
                        Weather::getTemperature
                ));
    }

    public static void main(String[] args) {
        List<Weather> weathers = List.of(
                new Weather("Amur", 22),
                new Weather("Arkhangelsk", 20),
                new Weather("Astrakhan", 25),
                new Weather("Belgorod", 19),
                new Weather("Bryansk", 20),
                new Weather("Chelyabinsk", 19)
        );

        System.out.println("Average temperature is: " + averageTemperature(weathers));

        System.out.println(
                "Regions with temperature equal or greater than 20 degrees: " +
                        findHotRegions(weathers, 20)
        );

        System.out.println(
                "Weather in regions represented as a map: " +
                        listToIdMap(weathers)
        );

        System.out.println(
                "Weather in regions represented as a map-list: " +
                        listToTemperatureMap(weathers)
        );
    }
}
