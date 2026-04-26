package escolaDanca.back.bd.repository;

import escolaDanca.back.bd.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {

    Optional<AlunoEntity> findByCpf(String cpf);

    List<AlunoEntity> findAllByAtivoTrue();

    boolean existsByCpf(String cpf);
}
