package diegobasili.gestioneViaggiAziendali.repositories;

import diegobasili.gestioneViaggiAziendali.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ViaggiRepository extends JpaRepository<Viaggio, UUID> {
}
