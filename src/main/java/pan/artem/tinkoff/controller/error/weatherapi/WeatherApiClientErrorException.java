package pan.artem.tinkoff.controller.error.weatherapi;

import lombok.Getter;

@Getter
public class WeatherApiClientErrorException extends WeatherApiException {

    private final int statusCode;
    private final int errorCode;

    public WeatherApiClientErrorException(String message, int statusCode, int errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
