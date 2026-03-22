package escolaDanca.back.domain.dto.cobranca;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class ConsultarCobrancaResponseDto{

    private List<CobrancaDto> cobrancas;
    private int numeroRegistros;

}
