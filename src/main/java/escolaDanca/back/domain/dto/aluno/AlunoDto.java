package escolaDanca.back.domain.dto.aluno;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoDto {
    private Long id;
    private String nome;
    private String cpf;
}
