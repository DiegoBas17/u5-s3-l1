package diegobasili.gestioneViaggiAziendali.services;

import diegobasili.gestioneViaggiAziendali.entities.Viaggio;
import diegobasili.gestioneViaggiAziendali.enums.StatoViaggio;
import diegobasili.gestioneViaggiAziendali.exceptions.BadRequestException;
import diegobasili.gestioneViaggiAziendali.exceptions.NotFoundException;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteDTO;
import diegobasili.gestioneViaggiAziendali.payloads.ViaggioDTO;
import diegobasili.gestioneViaggiAziendali.payloads.ViaggioStatoDTO;
import diegobasili.gestioneViaggiAziendali.repositories.ViaggiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
public class ViaggiService {
    @Autowired
    ViaggiRepository viaggiRepository;

    public Page<Viaggio> findAll(int page, int size, String sortBy) {
        if (page>20) page=20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggiRepository.findAll(pageable);
    }

    public Viaggio saveViaggio(ViaggioDTO body) {
        StatoViaggio statoViaggio;
        LocalDate dataViaggio;
        try {
            statoViaggio = StatoViaggio.valueOf(body.statoViaggio().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Stato del viaggio non valido: " + body.statoViaggio() + " il valore inserito deve essere: IN_PROGRAMMA o COMPLETATO!");
        }
        try {
            dataViaggio = LocalDate.parse(body.data());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Formato della data non valido: " + body.data() + ", il formato deve essere yyyy-MM-dd!");
        }
        Viaggio viaggio =  new Viaggio(body.destinazione(), dataViaggio, statoViaggio);
        return this.viaggiRepository.save(viaggio);
    }

    public Viaggio findById(UUID viaggioId) {
        return this.viaggiRepository.findById(viaggioId).orElseThrow(()-> new NotFoundException(viaggioId));
    }

    public Viaggio findByIdAndUpdate(UUID viaggioId, ViaggioDTO updateBody) {
        StatoViaggio statoViaggio;
        LocalDate dataViaggio;
        try {
            statoViaggio = StatoViaggio.valueOf(updateBody.statoViaggio().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Stato del viaggio non valido: " + updateBody.statoViaggio() + " il valore inserito deve essere: IN_PROGRAMMA o COMPLETATO!");
        }
        try {
            dataViaggio = LocalDate.parse(updateBody.data());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Formato della data non valido: " + updateBody.data() + ", il formato deve essere yyyy-MM-dd!");
        }
        Viaggio viaggio = findById(viaggioId);
        viaggio.setData(dataViaggio);
        viaggio.setStato_viaggio(statoViaggio);
        viaggio.setDestinazione(updateBody.destinazione());
        return viaggio;
    }

    public void findByIdAndDelete(UUID viaggioId) {
        Viaggio found = findById(viaggioId);
        this.viaggiRepository.delete(found);
    }

    public Viaggio changeStatoViaggio (UUID viaggioID, ViaggioStatoDTO viaggio) {
        Viaggio viaggio1 = findById(viaggioID);
        StatoViaggio statoViaggio;
        try {
            statoViaggio = StatoViaggio.valueOf(viaggio.statoViaggio().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Stato del viaggio non valido: " + viaggio.statoViaggio() + " il valore inserito deve essere: IN_PROGRAMMA o COMPLETATO!");
        }
        viaggio1.setStato_viaggio(statoViaggio);
        viaggiRepository.save(viaggio1);
        return viaggio1;
    }
}
