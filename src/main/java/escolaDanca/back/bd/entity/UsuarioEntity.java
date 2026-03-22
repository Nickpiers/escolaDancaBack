package escolaDanca.back.bd.entity;

import escolaDanca.back.domain.enums.Role;
import escolaDanca.back.security.CpfCrypto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @OneToOne(mappedBy = "usuario")
    private AlunoEntity aluno;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private Role tipoUsuario;

    @Convert(converter = CpfCrypto.class)
    @Column(nullable = false, unique = true)
    private String cpf;
}
