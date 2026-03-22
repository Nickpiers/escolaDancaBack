package escolaDanca.back.domain;

import org.springframework.http.HttpStatus;

public class ResponseFactory {

    public static ApiResponse success(HttpStatus status, String message) {
        return new ApiResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                null
        );
    }

    public static ApiResponse success(HttpStatus status, Object data) {
        return new ApiResponse(
                status.value(),
                status.getReasonPhrase(),
                null,
                data
        );
    }

    public static ApiResponse error(HttpStatus status, String message) {
        return new ApiResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                null
        );
    }
}
