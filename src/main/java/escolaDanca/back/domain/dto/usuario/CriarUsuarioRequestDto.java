package escolaDanca.back.domain.dto.usuario;

import escolaDanca.back.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioRequestDto(
        @NotBlank
        String cpf,

        @NotBlank
        String senha,

        @NotBlank
        String email,

        @NotBlank
        Role tipoUsuario){
}
