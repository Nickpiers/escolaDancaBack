package escolaDanca.back.bd.entity;

import escolaDanca.back.security.CpfCrypto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "aluno")
public class AlunoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno")
    private Long idAluno;

    @OneToOne(mappedBy = "aluno", fetch = FetchType.LAZY)
    private MatriculaEntity matricula;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", unique = true)
    private UsuarioEntity usuario;

    @Convert(converter = CpfCrypto.class)
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column
    private String telefone;

    @Column(nullable = false)
    private Boolean ativo = true;
}
