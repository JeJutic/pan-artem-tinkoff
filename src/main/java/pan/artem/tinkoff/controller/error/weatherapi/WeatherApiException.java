package pan.artem.tinkoff.controller.error.weatherapi;

import pan.artem.tinkoff.controller.error.MyAppException;

public class WeatherApiException extends MyAppException {

    public WeatherApiException() {
    }

    public WeatherApiException(String message) {
        super(message);
    }

    public WeatherApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
