package diegobasili.gestioneViaggiAziendali.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class) // Nelle parentesi indco quale eccezione debba venir gestita da questo metodo
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Lo status code deve essere 400
    public ErrorsPayload handleBadRequest(BadRequestException ex){
        // Questi handler mi consentono anche di accedere all'eccezione, utile per prendere il messaggio ad es
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsPayload handleNotFound(NotFoundException ex){
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsPayload handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorsPayload("Non hai i permessi per accedere", LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorsPayload handleGenericErrors(Exception ex){
        ex.printStackTrace(); // Non dimentichiamoci che è ESTREMAMENTE UTILE sapere dove è stato generata l'eccezione per poterla fixare
        return new ErrorsPayload("Problema lato server, giuro che lo risolveremo presto!", LocalDateTime.now());
    }
}
