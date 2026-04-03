package escolaDanca.back.domain.dto;

import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import escolaDanca.back.domain.dto.evento.ListarEventosResponseDto;
import escolaDanca.back.domain.dto.usuario.ConsultarUsuarioResponseDto;
import escolaDanca.back.domain.enums.Role;
import jakarta.annotation.Nullable;

public record LoginResponseDto(
        String token,
        String tipo,
        Role tipoUsuario,
        @Nullable ConsultarUsuarioResponseDto usuario,
        @Nullable ListarEventosResponseDto eventos,
        @Nullable ConsultarCobrancaResponseDto cobrancas
) {}


