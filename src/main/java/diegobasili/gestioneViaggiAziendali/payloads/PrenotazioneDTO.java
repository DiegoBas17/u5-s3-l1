package diegobasili.gestioneViaggiAziendali.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(@NotEmpty(message = "data obbligatorio!")
                              @Size(min = 10, max = 10)
                              String dataRichiesta,
                              @Size(min = 0, max = 250)
                              String note,
                              @NotEmpty(message = "data obbligatorio!")
                              String dipendeteID,
                              @NotEmpty(message = "data obbligatorio!")
                              String viaggioId) {
}
