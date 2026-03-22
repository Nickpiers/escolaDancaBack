package escolaDanca.back.resource.evento.controller;

import escolaDanca.back.domain.ApiResponse;
import escolaDanca.back.domain.ResponseFactory;
import escolaDanca.back.domain.dto.evento.CriarEventoRequestDto;
import escolaDanca.back.domain.dto.evento.ListarEventosResponseDto;
import escolaDanca.back.resource.evento.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static escolaDanca.back.domain.ApiStatus.CREATED;
import static escolaDanca.back.domain.ApiStatus.DELETED;
import static escolaDanca.back.domain.ApiStatus.OK;

@RestController
@RequestMapping("/api/evento")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping(value = "/criar")
    public ResponseEntity<ApiResponse> criarEvento(@RequestBody CriarEventoRequestDto request) {

        eventoService.criarEvento(request);
        return ResponseEntity.status(CREATED.getHttpStatus()).body(
                ResponseFactory.success(CREATED.getHttpStatus(), "Evento criado com sucesso"));
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<ApiResponse> listarEventos() {

        ListarEventosResponseDto listaEventos = eventoService.listarEventos();
        return ResponseEntity.ok().body(
                ResponseFactory.success(OK.getHttpStatus(), listaEventos));

    }

    @DeleteMapping(value = "/deletar/{idEvento}")
    public ResponseEntity<ApiResponse> deletarEvento(@PathVariable Integer idEvento) {

        eventoService.deletarEvento(idEvento);
        return ResponseEntity.status(DELETED.getHttpStatus()).body(
                ResponseFactory.success(DELETED.getHttpStatus(), "Evento deletado com sucesso"));
    }
}
