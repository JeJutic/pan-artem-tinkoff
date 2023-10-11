package pan.artem.tinkoff.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pan.artem.tinkoff.dto.ErrorInfo;
import pan.artem.tinkoff.exception.MyAppException;
import pan.artem.tinkoff.exception.RequestNotPermitted;
import pan.artem.tinkoff.exception.ResourceNotFoundException;
import pan.artem.tinkoff.exception.externalservice.ExternalServiceInternalErrorException;
import pan.artem.tinkoff.exception.externalservice.ExternalServiceLimitExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MyAppException.class)
    public ResponseEntity<ErrorInfo> handleMyAppException(HttpServletRequest request, MyAppException e) {
        logger.warn("Uncaught exception from request: {}", request.getRequestURL(), e);

        return ResponseEntity.internalServerError().body(
                new ErrorInfo(request.getRequestURL().toString(),
                        "Internal error occurred")
        );
    }

    public static String methodArgumentNotValidString(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        e.getFieldErrors()
                .forEach(fieldError ->
                        sb.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage())
                );
        return sb.toString();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentNotValid(
            HttpServletRequest request, MethodArgumentNotValidException e
    ) {
        return ResponseEntity.badRequest().body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Data format is violated: " +
                                GlobalExceptionHandler.methodArgumentNotValidString(e)
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleNotFound(
            HttpServletRequest request, ResourceNotFoundException e
    ) {
        return ResponseEntity.status(404).body(
                new ErrorInfo(request.getRequestURL().toString(),
                        "Specified resource was not found: " + e.getMessage())
        );
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorInfo> handleRequestNotPermitted(
            HttpServletRequest request, RequestNotPermitted e
    ) {
        logger.info("Rate limiter for current weather service has probably exceeded", e);

        return ResponseEntity.status(403).body(
                new ErrorInfo(request.getRequestURL().toString(),
                        "Request could not be permitted. " +
                                "There may be too many requests for this service."
                )
        );
    }

    @ExceptionHandler(ExternalServiceLimitExceededException.class)
    public ResponseEntity<ErrorInfo> handleLimitExceeded(
            HttpServletRequest request,
            ExternalServiceLimitExceededException e
    ) {
        logger.warn("Weather API call limit exceeded", e);

        return ResponseEntity.status(503).body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Service is temporarily unavailable"
                )
        );
    }

    @ExceptionHandler(ExternalServiceInternalErrorException.class)
    public ResponseEntity<ErrorInfo> handleInternalError(HttpServletRequest request) {
        return ResponseEntity.status(503).body(
                new ErrorInfo(
                        request.getRequestURL().toString(),
                        "Service is temporarily unavailable"
                )
        );
    }
}
