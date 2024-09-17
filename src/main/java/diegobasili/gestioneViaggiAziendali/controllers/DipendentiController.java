package diegobasili.gestioneViaggiAziendali.controllers;

import diegobasili.gestioneViaggiAziendali.entities.Dipendente;
import diegobasili.gestioneViaggiAziendali.exceptions.BadRequestException;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteDTO;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteRespDTO;
import diegobasili.gestioneViaggiAziendali.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {
    @Autowired
    private DipendentiService dipendentiService;

    @GetMapping
    //@PreAuthorize("hasAuthority('ADMIN')") // Solo gli admin possono leggere l'elenco degli utenti
    public Page<Dipendente> findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy){
        return this.dipendentiService.findAll(page, size, sortBy);
    }

  /*  @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteRespDTO save(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new DipendenteRespDTO (this.dipendentiService.saveDipendente(body).getId());
        }
    }*/

    @GetMapping("/{dipendenteId}")
    public Dipendente findById(@PathVariable UUID dipendenteId){
        return this.dipendentiService.findById(dipendenteId);
    }

    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')") // Solo gli admin possono modificare gli utenti
    public Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId, @RequestBody @Validated DipendenteDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return this.dipendentiService.findByIdAndUpdate(dipendenteId, body);
        }
    }

    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')") // Solo gli admin possono cancellare altri utenti
    public void findByIdAndDelete(@PathVariable UUID dipendenteId){
        this.dipendentiService.findByIdAndDelete(dipendenteId);
    }

    @PutMapping("/{dipendenteId}/avatar")
    public Dipendente uploadAvatar(@PathVariable UUID dipendenteId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.dipendentiService.uploadImage(dipendenteId, image);
    }

    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        // Tramite @AuthenticationPrincipal posso accedere ai dati dell'utente che sta effettuando la richiesta
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser, @RequestBody DipendenteDTO body) {
        return this.dipendentiService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        this.dipendentiService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

}
