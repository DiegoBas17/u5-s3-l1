package diegobasili.gestioneViaggiAziendali.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import diegobasili.gestioneViaggiAziendali.entities.Dipendente;
import diegobasili.gestioneViaggiAziendali.exceptions.BadRequestException;
import diegobasili.gestioneViaggiAziendali.exceptions.NotFoundException;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteDTO;
import diegobasili.gestioneViaggiAziendali.repositories.DipendentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DipendentiService {
    @Autowired
    private DipendentiRepository dipendentiRepository;
    @Autowired
    private Cloudinary cloudinary;

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if (page>20) page=20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendentiRepository.findAll(pageable);
    }

    public Dipendente saveDipendente(DipendenteDTO body) {
        if (body == null) {
            throw new BadRequestException("L'email deve avere un body!");
        }else if (this.dipendentiRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'email " + body.email() + " è già in uso!");
        } else {
            Dipendente dipendente = new Dipendente(body.username(), body.nome(), body.cognome(), body.email(), "https://ui-avatars.com/api/?name="+body.nome()+"+"+body.cognome());
            return this.dipendentiRepository.save(dipendente);
        }
    }

    public Dipendente findById(UUID dipendenteId) {
        return this.dipendentiRepository.findById(dipendenteId).orElseThrow(()-> new NotFoundException(dipendenteId));
    }

    public Dipendente findByIdAndUpdate(UUID dipendenteId, DipendenteDTO updateBody) {
        if (this.dipendentiRepository.existsByEmail(updateBody.email())) {
            throw new BadRequestException("L'email " + updateBody.email() + " è già in uso!");
        } else {
            Dipendente found = findById(dipendenteId);
            found.setAvatar("https://ui-avatars.com/api/?name="+updateBody.nome()+"+"+updateBody.cognome());
            found.setNome(updateBody.nome());
            found.setCognome(updateBody.cognome());
            found.setEmail(updateBody.email());
            return found;
        }
    }

    public void findByIdAndDelete(UUID dipendenteId) {
        Dipendente found = findById(dipendenteId);
        this.dipendentiRepository.delete(found);
    }

    public Dipendente uploadImage(UUID dipendenteId, MultipartFile file) throws IOException {
        Dipendente dipendente = findById(dipendenteId);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        dipendente.setAvatar(url);
        dipendentiRepository.save(dipendente);
        return dipendente;
    }
}
