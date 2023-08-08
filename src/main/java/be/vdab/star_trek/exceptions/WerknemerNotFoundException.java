package be.vdab.star_trek.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WerknemerNotFoundException extends RuntimeException{
    public WerknemerNotFoundException(long id) {
        super("Werknemer niet gevonden, Id: " + id);
    }
}
