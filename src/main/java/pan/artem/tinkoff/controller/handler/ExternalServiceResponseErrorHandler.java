package pan.artem.tinkoff.controller.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.exception.externalservice.ExternalServiceClientErrorException;
import pan.artem.tinkoff.exception.externalservice.ExternalServiceInternalErrorException;
import pan.artem.tinkoff.exception.externalservice.ExternalServiceLimitExceededException;

import java.io.IOException;

public class ExternalServiceResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() ||
                response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        var statusCode = response.getStatusCode();

        if (statusCode.is5xxServerError()) {
            throw new ExternalServiceInternalErrorException(response.getStatusText());
        } else if (statusCode.is4xxClientError()) {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper
                    .readTree(response.getBody())
                    .path("error");
            int errorCode = root.path("code").asInt();
            String message = root.path("message").asText();

            if ((statusCode == HttpStatus.BAD_REQUEST && errorCode == 1006) ||
                    (statusCode == HttpStatus.FORBIDDEN && errorCode == 2009)) {
                throw new ResourceNotFoundException(message);
            } else if (statusCode == HttpStatus.FORBIDDEN && errorCode == 2007) {
                throw new ExternalServiceLimitExceededException(message);
            } else if (statusCode == HttpStatus.BAD_REQUEST && errorCode == 9999) {
                throw new ExternalServiceInternalErrorException(message);
            } else {
                throw new ExternalServiceClientErrorException(
                        message, statusCode.value(), errorCode
                );
            }
        }
    }
}
