package escolaDanca.back.resource;

import escolaDanca.back.domain.ApiResponse;
import escolaDanca.back.domain.ResponseFactory;
import escolaDanca.back.domain.dto.LoginRequestDto;
import escolaDanca.back.domain.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static escolaDanca.back.domain.ApiStatus.OK;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto request) {

        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok().body(
                ResponseFactory.success(OK.getHttpStatus(), response));
    }

}
