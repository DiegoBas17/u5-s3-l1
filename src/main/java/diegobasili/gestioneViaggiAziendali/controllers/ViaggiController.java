package diegobasili.gestioneViaggiAziendali.controllers;

import diegobasili.gestioneViaggiAziendali.entities.Dipendente;
import diegobasili.gestioneViaggiAziendali.entities.Viaggio;
import diegobasili.gestioneViaggiAziendali.exceptions.BadRequestException;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteDTO;
import diegobasili.gestioneViaggiAziendali.payloads.ViaggioDTO;
import diegobasili.gestioneViaggiAziendali.payloads.ViaggioRespDTO;
import diegobasili.gestioneViaggiAziendali.payloads.ViaggioStatoDTO;
import diegobasili.gestioneViaggiAziendali.services.ViaggiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/viaggi")
public class ViaggiController {
    @Autowired
    private ViaggiService viaggiService;

    @GetMapping
    public Page<Viaggio> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy){
        return this.viaggiService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ViaggioRespDTO save(@RequestBody @Validated ViaggioDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new ViaggioRespDTO(this.viaggiService.saveViaggio(body).getId());
        }
    }

    @GetMapping("/{viaggioId}")
    public Viaggio findById(@PathVariable UUID viaggioId){
        return this.viaggiService.findById(viaggioId);
    }

    @PutMapping("/{viaggioId}")
    public Viaggio findByIdAndUpdate(@PathVariable UUID viaggioId, @RequestBody @Validated ViaggioDTO body, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return this.viaggiService.findByIdAndUpdate(viaggioId, body);
        }
    }

    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID viaggioId){
        this.viaggiService.findByIdAndDelete(viaggioId);
    }

    @PatchMapping("/{viaggioId}")
    public Viaggio changeStatoViaggio (@PathVariable UUID viaggioId, @RequestBody @Validated ViaggioStatoDTO body) {
        return this.viaggiService.changeStatoViaggio(viaggioId,body);
    }
}
