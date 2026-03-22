package escolaDanca.back.bd.repository;

import escolaDanca.back.bd.entity.CobrancaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CobrancaRepository extends JpaRepository<CobrancaEntity, Long> {

    Optional<CobrancaEntity> findTopByMatriculaAlunoUsuarioIdUsuarioOrderByCriadoEmDesc(Long idUsuario);

    @Query("""
        SELECT c FROM CobrancaEntity c
        JOIN c.matricula m
        JOIN m.aluno a
        JOIN a.usuario u
        WHERE u.idUsuario = :idUsuario
          AND c.criadoEm >= :dataInicio
        ORDER BY c.criadoEm DESC
        """)
    List<CobrancaEntity> findCobrancasUltimoAnoByUsuario(Long idUsuario, LocalDateTime dataInicio);
}
