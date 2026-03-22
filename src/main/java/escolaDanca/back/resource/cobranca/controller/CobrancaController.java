package escolaDanca.back.resource.cobranca.controller;

import escolaDanca.back.domain.ApiResponse;
import escolaDanca.back.domain.ResponseFactory;
import escolaDanca.back.domain.dto.cobranca.ConsultarCobrancaResponseDto;
import escolaDanca.back.resource.cobranca.service.CobrancaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static escolaDanca.back.domain.ApiStatus.OK;

@RestController
@RequestMapping("/api/cobranca")
@RequiredArgsConstructor
public class CobrancaController {

    private final CobrancaService cobrancaService;

    @GetMapping(value = "/consultar/{idUsuario}")
    public ResponseEntity<ApiResponse> consultarUltimaCobranca(@PathVariable Long idUsuario) {

        ConsultarCobrancaResponseDto cobranca = cobrancaService.consultarUltimaCobranca(idUsuario);
        return ResponseEntity.ok().body(
                ResponseFactory.success(OK.getHttpStatus(), cobranca));

    }

    @GetMapping(value = "/historico/{idUsuario}")
    public ResponseEntity<ApiResponse> consultarCobrancaPeriodoAno(
            @PathVariable Long idUsuario,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio) {

        var cobrancas = cobrancaService.consultarCobrancaPeriodoAno(idUsuario, dataInicio);
        return ResponseEntity.ok().body(
                ResponseFactory.success(OK.getHttpStatus(), cobrancas));

    }
}
