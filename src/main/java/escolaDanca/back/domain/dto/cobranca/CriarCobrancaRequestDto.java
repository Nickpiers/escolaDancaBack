package escolaDanca.back.domain.dto.cobranca;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CriarCobrancaRequestDto(
        @NotBlank
        String cpf,

        @NotBlank
        BigDecimal valorTotal,

        @NotBlank
        Long codigoInterno) {
}
