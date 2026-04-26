package escolaDanca.back.resource.evento.service;

import escolaDanca.back.bd.entity.EventoEntity;
import escolaDanca.back.bd.repository.EventoRespository;
import escolaDanca.back.domain.dto.evento.CriarEventoRequestDto;
import escolaDanca.back.domain.dto.evento.EventoDto;
import escolaDanca.back.domain.dto.evento.ListarEventosResponseDto;
import escolaDanca.back.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRespository eventoRespository;

    public void criarEvento(CriarEventoRequestDto request) {

        EventoEntity evento = new EventoEntity();

        evento.setNomeEvento(request.nomeEvento());
        evento.setDescricaoEvento(request.descricaoEvento());
        evento.setLocalEvento(request.localEvento());
        evento.setMomentoEvento(request.momentoEvento());
        eventoRespository.save(evento);
    }

    public ListarEventosResponseDto listarEventos() {
        List<EventoEntity> eventos = eventoRespository.findAll();

        List<EventoDto> eventoDtos = eventos.stream()
                .map(e -> {
                    EventoDto dto = new EventoDto();
                    dto.setIdEvento(e.getIdEvento());
                    dto.setNome(e.getNomeEvento());
                    dto.setDescricao(e.getDescricaoEvento());
                    dto.setLocal(e.getLocalEvento());

                    dto.setData(e.getMomentoEvento().toLocalDate().toString());
                    dto.setHora(e.getMomentoEvento().toLocalTime().toString());

                    return dto;
                })
                .toList();

        ListarEventosResponseDto response = new ListarEventosResponseDto();
        response.setEventos(eventoDtos);
        response.setNumeroEventos(eventoDtos.size());
        response.setIdProximoEvento(getIdProximoEvento(eventoDtos));

        return response;
    }

    private int getIdProximoEvento(List<EventoDto> eventoDtos) {
        LocalDateTime agora = LocalDateTime.now();

        return eventoDtos.stream()
                .map(dto -> {
                    LocalDate data = LocalDate.parse(dto.getData());
                    LocalTime hora = LocalTime.parse(dto.getHora());
                    LocalDateTime momento = LocalDateTime.of(data, hora);

                    return new AbstractMap.SimpleEntry<>(dto.getIdEvento(), momento);
                })
                .filter(entry -> entry.getValue().isAfter(agora))
                .min(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(-1);
    }


    public void deletarEvento(Integer idEvento) {
        if (eventoRespository.existsById(idEvento)) {
            eventoRespository.deleteById(idEvento);
        } else {
            throw new ResourceNotFoundException("Evento não encontrado com id: " + idEvento);
        }
    }
}
