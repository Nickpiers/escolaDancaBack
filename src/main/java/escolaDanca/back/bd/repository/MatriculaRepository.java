package escolaDanca.back.bd.repository;

import escolaDanca.back.bd.entity.AlunoEntity;
import escolaDanca.back.bd.entity.MatriculaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<MatriculaEntity, Long> {

    Optional<MatriculaEntity> findByAluno(AlunoEntity aluno);

}
