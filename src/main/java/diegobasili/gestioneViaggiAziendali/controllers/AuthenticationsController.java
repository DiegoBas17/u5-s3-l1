package diegobasili.gestioneViaggiAziendali.controllers;

import diegobasili.gestioneViaggiAziendali.exceptions.BadRequestException;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteDTO;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteLoginDTO;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteLoginRespDTO;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteRespDTO;
import diegobasili.gestioneViaggiAziendali.services.AuthenticationsService;
import diegobasili.gestioneViaggiAziendali.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("authorizations")
public class AuthenticationsController {
    @Autowired
    private DipendentiService dipendentiService;
    @Autowired
    private AuthenticationsService authenticationsService;

    @PostMapping("/login")
    public DipendenteLoginRespDTO login(@RequestBody DipendenteLoginDTO payload) {
        return new DipendenteLoginRespDTO(this.authenticationsService.checkCredentialsAndGenerateToken(payload));
    }

    // 2. POST http://localhost:3001/users (+req.body)
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteRespDTO save(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        // @Validated serve per 'attivare' le regole di validazione descritte nel DTO
        // BindingResult mi permette di capire se ci sono stati errori e quali errori ci sono stati

        if (validationResult.hasErrors()) {
            // Se ci sono stati errori lanciamo un'eccezione custom
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Se non ci sono stati salviamo l'utente

            return new DipendenteRespDTO(this.dipendentiService.saveDipendente(body).getId());
        }

    }
}
