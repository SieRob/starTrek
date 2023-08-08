package be.vdab.star_trek.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BestellingenMetNaam (long id, String omschrijving, BigDecimal bedrag, LocalDateTime moment,
                                   String voornaam, String familienaam){
}