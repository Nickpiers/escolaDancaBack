package escolaDanca.back.domain.dto;

import escolaDanca.back.domain.dto.aluno.ConsultarAlunoResponseDto;
import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import escolaDanca.back.domain.dto.evento.ListarEventosResponseDto;
import escolaDanca.back.domain.enums.Role;
import jakarta.annotation.Nullable;

public record LoginResponseDto(
        String token,
        String tipo,
        Role tipoUsuario,
        @Nullable ConsultarAlunoResponseDto aluno,
        @Nullable ListarEventosResponseDto eventos,
        @Nullable ConsultarCobrancaResponseDto cobrancas
) {}


