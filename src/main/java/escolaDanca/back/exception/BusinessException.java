package escolaDanca.back.exception;

import static escolaDanca.back.domain.ApiStatus.BAD_REQUEST;

public class BusinessException extends ApplicationException {
    public BusinessException(String message) {
        super(BAD_REQUEST.getHttpStatus(), message);
    }
}
