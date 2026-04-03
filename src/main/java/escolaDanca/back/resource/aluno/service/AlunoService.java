package escolaDanca.back.resource.aluno.service;

import escolaDanca.back.bd.entity.AlunoEntity;
import escolaDanca.back.bd.entity.MatriculaEntity;
import escolaDanca.back.bd.repository.AlunoRepository;
import escolaDanca.back.bd.repository.MatriculaRepository;
import escolaDanca.back.domain.dto.aluno.AlunoDto;
import escolaDanca.back.domain.dto.aluno.ConsultarAlunoResponseDto;
import escolaDanca.back.domain.dto.aluno.CriarAlunoRequestDto;
import escolaDanca.back.domain.dto.aluno.ListarAlunosResponseDto;
import escolaDanca.back.exception.BusinessException;
import escolaDanca.back.exception.ResourceNotFoundException;
import escolaDanca.back.resource.cobranca.service.CobrancaService;
import escolaDanca.back.utils.MascararCpf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;
    private final CobrancaService cobrancaService;

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

    public ListarAlunosResponseDto listarAlunos() {
        List<AlunoEntity> alunos = alunoRepository.findAllByAtivoTrue();

        List<AlunoDto> alunosDtos = alunos.stream()
                .map(e -> {
                    AlunoDto dto = new AlunoDto();
                    dto.setId(e.getIdAluno());
                    dto.setNome(e.getNome());
                    dto.setCpf(e.getCpf());

                    return dto;
                })
                .toList();

        ListarAlunosResponseDto response = new ListarAlunosResponseDto();
        response.setAlunos(alunosDtos);
        response.setNumeroAlunos(alunosDtos.size());

        return response;
    }

    public ConsultarAlunoResponseDto consultarAluno(String cpf) {

        String cpfMascarado = MascararCpf.mascararCpf(cpf);

        AlunoEntity aluno = alunoRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno nao encontrado: " + cpfMascarado));

        ConsultarAlunoResponseDto response = new ConsultarAlunoResponseDto();
        response.setId(aluno.getIdAluno());
        response.setNome(aluno.getNome());
        response.setCpf(aluno.getCpf());

        return response;
    }
}
