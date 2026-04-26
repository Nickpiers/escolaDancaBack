package escolaDanca.back.domain.dto.evento;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class EventoDto {

    private Integer idEvento;
    private String nome;
    private String descricao;
    private String local;
    private String data;
    private String hora;

}
