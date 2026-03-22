package escolaDanca.back.exception;

import static escolaDanca.back.domain.ApiStatus.NOT_FOUND;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(NOT_FOUND.getHttpStatus(), message);
    }
}
