package escolaDanca.back.domain;

public record ApiResponse(
        int status,
        String type,
        String message,
        Object data) {
}
