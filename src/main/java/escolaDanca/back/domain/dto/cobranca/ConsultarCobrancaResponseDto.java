package escolaDanca.back.domain.dto.cobranca;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class ConsultarCobrancaResponseDto{

    private List<CobrancaDto> cobrancasEmAberto;
    private List<CobrancaDto> cobrancasPagas;
    private int numeroRegistros;
    private Long idProximaCobranca;

}
