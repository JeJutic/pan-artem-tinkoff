package pan.artem.tinkoff.controller.error.weatherapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import pan.artem.tinkoff.controller.error.ResourceNotFoundException;

import java.io.IOException;

public class WeatherApiResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() ||
                response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        var statusCode = response.getStatusCode();

        if (statusCode.is5xxServerError()) {
            throw new WeatherApiInternalErrorException(response.getStatusText());
        } else if (statusCode.is4xxClientError()) {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper
                    .readTree(response.getBody())
                    .path("error");
            int errorCode = root.path("code").asInt();
            String message = root.path("message").asText();

            if ((statusCode.value() == 400 && errorCode == 1006) ||
                    (statusCode.value() == 403 && errorCode == 2009)) {
                throw new ResourceNotFoundException(message);
            } else if (statusCode.value() == 403 && errorCode == 2007) {
                throw new WeatherApiLimitExceededException(message);
            } else if (statusCode.value() == 400 && errorCode == 9999) {
                throw new WeatherApiInternalErrorException(message);
            } else {
                throw new WeatherApiClientErrorException(
                        message, statusCode.value(), errorCode
                );
            }
        }
    }
}
