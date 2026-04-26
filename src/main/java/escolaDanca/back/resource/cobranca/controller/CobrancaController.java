package escolaDanca.back.resource.cobranca.controller;

import escolaDanca.back.domain.ApiResponse;
import escolaDanca.back.domain.ResponseFactory;
import escolaDanca.back.domain.dto.cobranca.CriarCobrancaRequestDto;
import escolaDanca.back.resource.cobranca.service.CobrancaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static escolaDanca.back.domain.ApiStatus.CREATED;
import static escolaDanca.back.domain.ApiStatus.OK;

@RestController
@RequestMapping("/api/cobranca")
@RequiredArgsConstructor
public class CobrancaController {

    private final CobrancaService cobrancaService;

    @PostMapping(value = "/criar")
    public ResponseEntity<ApiResponse> criarCobranca(@RequestBody CriarCobrancaRequestDto request) {

        cobrancaService.criarCobranca(request);
        return ResponseEntity.status(CREATED.getHttpStatus()).body(
                ResponseFactory.success(CREATED.getHttpStatus(), "Cobrança criada com sucesso"));
    }

    @GetMapping(value = "/historico/{idAluno}")
    public ResponseEntity<ApiResponse> consultarCobrancaPeriodoAno(
            @PathVariable Long idAluno,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio) {

        var cobrancas = cobrancaService.consultarCobrancaPeriodoAno(idAluno, dataInicio);
        return ResponseEntity.ok().body(
                ResponseFactory.success(OK.getHttpStatus(), cobrancas));

    }
}
