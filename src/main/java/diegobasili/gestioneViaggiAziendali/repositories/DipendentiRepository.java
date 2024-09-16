package diegobasili.gestioneViaggiAziendali.repositories;

import diegobasili.gestioneViaggiAziendali.entities.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DipendentiRepository extends JpaRepository<Dipendente, UUID> {
    boolean existsByEmail(String email);
}
