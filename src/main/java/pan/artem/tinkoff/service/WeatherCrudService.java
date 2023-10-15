package pan.artem.tinkoff.service;

public interface WeatherCrudService<T, K> {

    K getWeather(String city);

    void addWeather(String city, T weatherDto);

    boolean updateWeather(String city, T weatherDto);

    void deleteWeathers(String city);
}
