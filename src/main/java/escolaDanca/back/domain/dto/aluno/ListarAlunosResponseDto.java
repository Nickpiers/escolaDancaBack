package escolaDanca.back.domain.dto.aluno;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListarAlunosResponseDto {
    private List<AlunoDto> alunos;
    private int numeroAlunos;
}
