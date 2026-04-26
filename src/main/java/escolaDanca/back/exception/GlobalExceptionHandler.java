package escolaDanca.back.exception;

import escolaDanca.back.domain.ApiResponse;
import escolaDanca.back.domain.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static escolaDanca.back.domain.ApiStatus.INTERNAL_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApplicationException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ResponseFactory.error(ex.getStatus(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(INTERNAL_ERROR.getHttpStatus())
                .body(ResponseFactory.error(
                        INTERNAL_ERROR.getHttpStatus(),
                        "Erro interno de servidor"
                ));
    }


}
