package diegobasili.gestioneViaggiAziendali.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Prenotazione {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDate data_richiesta;
    private String note;
    @ManyToOne
    private Dipendente dipendente;
    @ManyToOne
    private Viaggio viaggio;

    public Prenotazione(LocalDate data_richiesta, String note, Dipendente dipendente, Viaggio viaggio) {
        this.data_richiesta = data_richiesta;
        this.note = note;
        this.dipendente = dipendente;
        this.viaggio = viaggio;
    }
}
