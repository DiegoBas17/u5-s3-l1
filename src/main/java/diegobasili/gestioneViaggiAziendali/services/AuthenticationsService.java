package diegobasili.gestioneViaggiAziendali.services;

import diegobasili.gestioneViaggiAziendali.entities.Dipendente;
import diegobasili.gestioneViaggiAziendali.exceptions.UnauthorizedException;
import diegobasili.gestioneViaggiAziendali.payloads.DipendenteLoginDTO;
import diegobasili.gestioneViaggiAziendali.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationsService {
    @Autowired
    private DipendentiService dipendentiService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(DipendenteLoginDTO body) {
        // 1. Controllo le credenziali
        // 1.1 Cerco nel db tramite email se esiste l'utente
        Dipendente found = this.dipendentiService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            // 1.2 Se lo trovo verifico se la pw trovata combacia con quella passataci tramite body
            // 2. Se è tutto ok --> genero un access token e lo restituisco
            return jwtTools.createToken(found);
        } else {
            // 3. Se le credenziali sono errate --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali errate!");
        }


    }
}
