package escolaDanca.back.bd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "evento")
public class EventoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Integer idEvento;

    @Column(name = "nome_evento", nullable = false)
    private String nomeEvento;

    @Column(name = "descricao_evento", nullable = false)
    private String descricaoEvento;

    @Column(name = "local_evento", nullable = false)
    private String localEvento;

    @Column(name = "momento_evento", nullable = false)
    private LocalDateTime momentoEvento;
}
