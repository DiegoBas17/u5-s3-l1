package diegobasili.gestioneViaggiAziendali.repositories;

import diegobasili.gestioneViaggiAziendali.entities.Dipendente;
import diegobasili.gestioneViaggiAziendali.entities.Prenotazione;
import diegobasili.gestioneViaggiAziendali.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByDipendenteAndViaggioData(Dipendente dipendente, LocalDate data);
}
