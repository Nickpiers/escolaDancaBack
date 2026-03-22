package escolaDanca.back.domain.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioRequestDto(
        @NotBlank
        String cpf,

        @NotBlank
        String senha){
}
