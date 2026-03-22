package escolaDanca.back.resource.aluno.controller;

import escolaDanca.back.domain.ApiResponse;
import escolaDanca.back.domain.ResponseFactory;
import escolaDanca.back.domain.dto.aluno.CriarAlunoRequestDto;
import escolaDanca.back.domain.dto.aluno.ListarAlunosResponseDto;
import escolaDanca.back.resource.aluno.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static escolaDanca.back.domain.ApiStatus.CREATED;
import static escolaDanca.back.domain.ApiStatus.OK;

@RestController
@RequestMapping("/api/aluno")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping(value = "/criar")
    public ResponseEntity<ApiResponse> criarAluno(@RequestBody CriarAlunoRequestDto request) {

        alunoService.criarAluno(request);
        return ResponseEntity.status(CREATED.getHttpStatus()).body(
                ResponseFactory.success(CREATED.getHttpStatus(), "Aluno criado com sucesso"));

    }

    @GetMapping(value = "/listar")
    public ResponseEntity<ApiResponse> listarAlunos() {

        ListarAlunosResponseDto listaAlunos = alunoService.listarAlunos();
        return ResponseEntity.ok().body(
                ResponseFactory.success(OK.getHttpStatus(), listaAlunos));

    }

}
