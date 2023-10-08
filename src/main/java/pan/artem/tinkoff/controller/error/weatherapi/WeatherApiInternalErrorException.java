package pan.artem.tinkoff.controller.error.weatherapi;

public class WeatherApiInternalErrorException extends WeatherApiException {

    public WeatherApiInternalErrorException(String message) {
        super(message);
    }
}
