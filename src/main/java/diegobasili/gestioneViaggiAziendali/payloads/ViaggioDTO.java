package diegobasili.gestioneViaggiAziendali.payloads;

import diegobasili.gestioneViaggiAziendali.enums.StatoViaggio;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ViaggioDTO(@NotEmpty(message = "destinazione obbligatorio!")
                         @Size(min = 3, max = 40)
                         String destinazione,
                         @NotEmpty(message = "data obbligatorio!")
                         @Size(min = 10, max = 10)
                         String data,
                         @NotNull
                         String statoViaggio) {
}
