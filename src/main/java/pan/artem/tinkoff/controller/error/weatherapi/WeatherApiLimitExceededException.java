package pan.artem.tinkoff.controller.error.weatherapi;

public class WeatherApiLimitExceededException extends WeatherApiException {

    public WeatherApiLimitExceededException(String message) {
        super(message);
    }
}
