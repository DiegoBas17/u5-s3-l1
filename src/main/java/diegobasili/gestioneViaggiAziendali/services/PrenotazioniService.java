package diegobasili.gestioneViaggiAziendali.services;

import diegobasili.gestioneViaggiAziendali.entities.Dipendente;
import diegobasili.gestioneViaggiAziendali.entities.Prenotazione;
import diegobasili.gestioneViaggiAziendali.entities.Viaggio;
import diegobasili.gestioneViaggiAziendali.exceptions.BadRequestException;
import diegobasili.gestioneViaggiAziendali.exceptions.NotFoundException;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteDTO;
import diegobasili.gestioneViaggiAziendali.payloads.PrenotazioneDTO;
import diegobasili.gestioneViaggiAziendali.payloads.ViaggioDTO;
import diegobasili.gestioneViaggiAziendali.repositories.DipendentiRepository;
import diegobasili.gestioneViaggiAziendali.repositories.PrenotazioniRepository;
import diegobasili.gestioneViaggiAziendali.repositories.ViaggiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioniService {
    @Autowired
    private PrenotazioniRepository prenotazioniRepository;
    @Autowired
    private DipendentiRepository dipendentiRepository;
    @Autowired
    private ViaggiRepository viaggiRepository;

    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        if (page>20) page=20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioniRepository.findAll(pageable);
    }

    public Prenotazione savePrenotazione(PrenotazioneDTO body) {
        UUID dipendenteId;
        UUID viaggioId;
        try {
            dipendenteId = UUID.fromString(body.dipendeteID());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("ID dipendente non valido: " + body.dipendeteID() + ". Deve essere un UUID valido.");
        }
        try {
            viaggioId = UUID.fromString(body.viaggioId());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("ID viaggio non valido: " + body.viaggioId() + ". Deve essere un UUID valido.");
        }
        Dipendente dipendente = dipendentiRepository.findById(dipendenteId).orElseThrow(()-> new NotFoundException(dipendenteId));
        Viaggio viaggio = viaggiRepository.findById(viaggioId).orElseThrow(()-> new NotFoundException(viaggioId));
        List<Prenotazione> listaPrenotazioni = prenotazioniRepository.findByDipendenteAndViaggioData(dipendente, viaggio.getData());
        if (!listaPrenotazioni.isEmpty()) {
            throw new BadRequestException("esiste gia una prenotazione in questa data: " + viaggio.getData() + " per questo dipendente: " + dipendente.getCognome());
        }
        LocalDate dataViaggio;
        try {
            dataViaggio = LocalDate.parse(body.dataRichiesta());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Formato della data non valido: " + body.dataRichiesta() + ", il formato deve essere yyyy-MM-dd!");
        }
        Prenotazione prenotazione = new Prenotazione(dataViaggio, body.note(), dipendente, viaggio);
        return this.prenotazioniRepository.save(prenotazione);
    }

    public Prenotazione findById(UUID prenotazioneId) {
        return this.prenotazioniRepository.findById(prenotazioneId).orElseThrow(()-> new NotFoundException(prenotazioneId));
    }

    public Prenotazione findByIdAndUpdate(UUID prenotazioneId, PrenotazioneDTO updateBody) {
        UUID dipendenteId;
        UUID viaggioId;
        try {
            dipendenteId = UUID.fromString(updateBody.dipendeteID());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("ID dipendente non valido: " + updateBody.dipendeteID() + ". Deve essere un UUID valido.");
        }
        try {
            viaggioId = UUID.fromString(updateBody.viaggioId());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("ID viaggio non valido: " + updateBody.viaggioId() + ". Deve essere un UUID valido.");
        }
        Dipendente dipendente = dipendentiRepository.findById(dipendenteId).orElseThrow(()-> new NotFoundException(dipendenteId));
        Viaggio viaggio = viaggiRepository.findById(viaggioId).orElseThrow(()-> new NotFoundException(viaggioId));
        LocalDate dataViaggio;
        try {
            dataViaggio = LocalDate.parse(updateBody.dataRichiesta());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Formato della data non valido: " + updateBody.dataRichiesta() + ", il formato deve essere yyyy-MM-dd!");
        }
        Prenotazione found = findById(prenotazioneId);
        found.setNote(updateBody.note());
        found.setDipendente(dipendente);
        found.setViaggio(viaggio);
        found.setData_richiesta(dataViaggio);
        return found;
    }

    public void findByIdAndDelete(UUID prenotazioneId) {
        Prenotazione found = findById(prenotazioneId);
        this.prenotazioniRepository.delete(found);
    }
}
