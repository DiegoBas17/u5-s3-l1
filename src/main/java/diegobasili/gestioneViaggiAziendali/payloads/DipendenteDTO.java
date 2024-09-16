package diegobasili.gestioneViaggiAziendali.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(@NotEmpty(message = "username obbligatorio!")
                           @Size(min = 3, max = 40)
                           String username,
                            @NotEmpty(message = "nome obbligatorio!")
                           @Size(min = 3, max = 40)
                           String nome,
                            @NotEmpty(message = "cognome obbligatorio!")
                           @Size(min = 3, max = 40)
                           String cognome,
                            @Email(message = "email non valida!")
                           String email) {
}
