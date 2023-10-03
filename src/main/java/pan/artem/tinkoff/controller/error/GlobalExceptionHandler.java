package pan.artem.tinkoff.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MyAppException.class)
    public ResponseEntity<ErrorInfo> handleMyAppException(HttpServletRequest request) {
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
    public ResponseEntity<ErrorInfo> handle(
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
}
