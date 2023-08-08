package be.vdab.star_trek.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record NieuweBestelling (@NotNull Long werknemerId, @NotBlank String omschrijving, @NotNull @PositiveOrZero BigDecimal bedrag){
}
