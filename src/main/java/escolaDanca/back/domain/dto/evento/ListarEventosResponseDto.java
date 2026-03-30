package escolaDanca.back.domain.dto.evento;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class ListarEventosResponseDto {

    private List<EventoDto> eventos;
    private int numeroEventos;
    private int idProximoEvento;

}
