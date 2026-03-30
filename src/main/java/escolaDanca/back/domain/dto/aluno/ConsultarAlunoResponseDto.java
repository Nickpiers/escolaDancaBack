package escolaDanca.back.domain.dto.aluno;

import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultarAlunoResponseDto {
    private Long id;
    private String nome;
    private String cpf;
    private ConsultarCobrancaResponseDto ultimaCobranca;
}
