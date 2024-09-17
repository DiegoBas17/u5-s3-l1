package diegobasili.gestioneViaggiAziendali.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import diegobasili.gestioneViaggiAziendali.enums.Ruolo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Dipendente implements UserDetails {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;
    @JsonIgnore
    private String password;

    public Dipendente(String username, String nome, String cognome, String email, String avatar, String password) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.avatar = avatar;
        this.ruolo = Ruolo.USER;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
