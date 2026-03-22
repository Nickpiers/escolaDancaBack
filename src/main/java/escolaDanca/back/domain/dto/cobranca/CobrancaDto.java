package escolaDanca.back.domain.dto.cobranca;

import escolaDanca.back.domain.enums.StatusPagamento;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Setter
@Getter
public class CobrancaDto {

    private StatusPagamento statusPagamento;
    private BigDecimal totalCobranca;
    private BigDecimal totalPago;
    private String nomePagador;
    private String emailPagador;
    private String cpfPagador;
    private String nomeServico;
    private LocalDate vencimento;
    private String codigoBarras;
    private String linkBoleto;
    private LocalDateTime pagoEm;
    private String codigoPix;

}
