package be.vdab.star_trek.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OnvoldoendeBudgetException extends RuntimeException{
    public OnvoldoendeBudgetException(String voornaam, String achternaam) {
        super("Onvoldoende budget beschikbaar voor " + voornaam + " " + achternaam);
    }

    public OnvoldoendeBudgetException() {
        super("Onvoldoende Budget");
    }
}
