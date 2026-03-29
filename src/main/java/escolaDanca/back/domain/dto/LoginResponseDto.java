package escolaDanca.back.domain.dto;

import escolaDanca.back.domain.enums.Role;

public record LoginResponseDto(
        String token,
        String tipo,
        Role tipoUsuario) {
}
