package pan.artem.tinkoff.controller.error;

import lombok.Value;

@Value
public class ErrorInfo {
    String url;
    String error;
}
