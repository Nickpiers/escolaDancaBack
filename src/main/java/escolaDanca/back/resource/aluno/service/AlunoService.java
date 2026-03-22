package escolaDanca.back.resource.aluno.service;

import escolaDanca.back.bd.entity.AlunoEntity;
import escolaDanca.back.bd.entity.MatriculaEntity;
import escolaDanca.back.bd.repository.AlunoRepository;
import escolaDanca.back.bd.repository.MatriculaRepository;
import escolaDanca.back.domain.dto.aluno.CriarAlunoRequestDto;
import escolaDanca.back.exception.BusinessException;
import escolaDanca.back.utils.MascararCpf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;

    public void criarAluno(CriarAlunoRequestDto request) {

        String cpfMascarado = MascararCpf.mascararCpf(request.cpf());

        if (alunoRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF aluno já cadastrado: " + cpfMascarado);
        }

        AlunoEntity aluno = new AlunoEntity();
        aluno.setCpf(request.cpf());
        aluno.setNome(request.nome().toUpperCase());
        aluno.setEmail(request.email());
        aluno.setTelefone(request.telefone());
        aluno.setAtivo(true);

        alunoRepository.save(aluno);

        AlunoEntity alunoParaMatricula = alunoRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new BusinessException("Erro na criacao de matricula"));

        MatriculaEntity matricula = new MatriculaEntity();
        matricula.setAluno(alunoParaMatricula);
        matricula.setDataInicio(request.dataInicio());
        matricula.setDataTermino(request.dataTermino());
        matricula.setStatus("ATIVO");

        matriculaRepository.save(matricula);
    }
}
