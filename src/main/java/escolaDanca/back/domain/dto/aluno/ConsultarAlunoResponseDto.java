package escolaDanca.back.domain.dto.aluno;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultarAlunoResponseDto {
    private Long id;
    private String nome;
    private String cpf;
}
