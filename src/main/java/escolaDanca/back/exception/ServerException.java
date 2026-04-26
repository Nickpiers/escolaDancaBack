package escolaDanca.back.exception;

import static escolaDanca.back.domain.ApiStatus.INTERNAL_ERROR;

public class ServerException extends ApplicationException {
    public ServerException(String message) {
      super(INTERNAL_ERROR.getHttpStatus(), message);
    }
}
