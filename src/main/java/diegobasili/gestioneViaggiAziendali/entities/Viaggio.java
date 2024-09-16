package diegobasili.gestioneViaggiAziendali.entities;

import diegobasili.gestioneViaggiAziendali.enums.StatoViaggio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Viaggio {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private UUID id;
    private String destinazione;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private StatoViaggio stato_viaggio;

    public Viaggio(String destinazione, LocalDate data, StatoViaggio stato_viaggio) {
        this.destinazione = destinazione;
        this.data = data;
        this.stato_viaggio = stato_viaggio;
    }
}
