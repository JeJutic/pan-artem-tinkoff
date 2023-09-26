package pan.artem.tinkoff;

import pan.artem.tinkoff.domain.Weather;

import java.util.List;

import static pan.artem.tinkoff.WeatherUtils.*;

public class App {

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
