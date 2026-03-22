package escolaDanca.back.domain;

import org.springframework.http.HttpStatus;

public enum ApiStatus {

    CREATED(HttpStatus.CREATED),
    OK(HttpStatus.OK),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;

    ApiStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getCode() {
        return httpStatus.value();
    }

    public String getReason() {
        return httpStatus.getReasonPhrase();
    }
}
