package pan.artem.tinkoff.controller.error.weatherapi;

import pan.artem.tinkoff.controller.error.MyAppException;

public class WeatherApiException extends MyAppException {

    public WeatherApiException() {
    }

    public WeatherApiException(String message) {
        super(message);
    }

    public WeatherApiException(Throwable cause) {
        super(cause);
    }
}
