package escolaDanca.back.resource.usuario.controller;

import escolaDanca.back.domain.ApiResponse;
import escolaDanca.back.domain.ResponseFactory;
import escolaDanca.back.domain.dto.usuario.CriarUsuarioRequestDto;
import escolaDanca.back.resource.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static escolaDanca.back.domain.ApiStatus.CREATED;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping(value = "/criar")
    public ResponseEntity<ApiResponse> criarUsuario(@RequestBody CriarUsuarioRequestDto request) {

        usuarioService.criarUsuario(request);
        return ResponseEntity.status(CREATED.getHttpStatus()).body(
                ResponseFactory.success(CREATED.getHttpStatus(), "Usuário criado com sucesso"));

    }
}
