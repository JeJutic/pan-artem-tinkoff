package pan.artem.tinkoff.dto;

import lombok.Value;

@Value
public class ErrorInfo {
    String url;
    String error;
}
