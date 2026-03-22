package escolaDanca.back.bd.repository;

import escolaDanca.back.bd.entity.EventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRespository extends JpaRepository<EventoEntity, Integer> {
}
