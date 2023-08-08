package be.vdab.star_trek.domain;

import be.vdab.star_trek.exceptions.OnvoldoendeBudgetException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Werknemer {
    private final long id;
    private final String voornaam;
    private final String familienaam;
    private BigDecimal budget;

    public Werknemer(long id, String voornaam, String familienaam, BigDecimal budget) {
        this.id = id;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.budget = budget;
    }

    public Werknemer(String voornaam, String familienaam, BigDecimal budget) {
        this(0, voornaam, familienaam, budget);
    }

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void bestel(BigDecimal kost){
        if(kost.compareTo(budget) > 0){
            throw new OnvoldoendeBudgetException(getVoornaam(), getFamilienaam());
        }
        budget = budget.subtract(kost);
    }
}
