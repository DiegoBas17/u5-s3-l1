package diegobasili.gestioneViaggiAziendali.payloads;

import jakarta.validation.constraints.NotNull;

public record ViaggioStatoDTO(@NotNull String statoViaggio) {
}
