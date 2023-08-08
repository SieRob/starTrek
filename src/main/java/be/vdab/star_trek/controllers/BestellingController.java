package be.vdab.star_trek.controllers;

import be.vdab.star_trek.domain.NieuweBestelling;
import be.vdab.star_trek.dto.BestellingenMetNaam;
import be.vdab.star_trek.services.BestellingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bestellingen")
public class BestellingController {
    private BestellingService bestellingService;

    public BestellingController(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }

    @GetMapping("{id}")
    List<BestellingenMetNaam> findById(@PathVariable long id){
        return bestellingService.findById(id);
    }

    @PostMapping
    long bestel(@RequestBody @Valid NieuweBestelling bestelling){
        return bestellingService.bestel(bestelling);
    }

}
