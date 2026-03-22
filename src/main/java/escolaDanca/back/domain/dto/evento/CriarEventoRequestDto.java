package escolaDanca.back.domain.dto.evento;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CriarEventoRequestDto(
        @NotBlank
        String nomeEvento,

        @NotBlank
        String descricaoEvento,

        @NotBlank
        String localEvento,

        @NotBlank
        LocalDateTime momentoEvento) {
}
