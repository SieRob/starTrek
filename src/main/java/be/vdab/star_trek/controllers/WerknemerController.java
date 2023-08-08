package be.vdab.star_trek.controllers;

import be.vdab.star_trek.domain.Werknemer;
import be.vdab.star_trek.services.WerknemerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class WerknemerController {
    private final WerknemerService werknemerService;
    private record IdEnNaam(long id, String voornaam, String familienaam){
        IdEnNaam(Werknemer werknemer){
            this(werknemer.getId(), werknemer.getVoornaam(), werknemer.getFamilienaam());
        }
    }
    public WerknemerController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }

    @GetMapping("index")
    Stream<IdEnNaam> findAll(){
        return werknemerService.findAll()
                .stream()
                .map(werknemer -> new IdEnNaam(werknemer));
    }
    /** backup in case stuff breaks */
    /*
    @GetMapping("werknemers/{werknemerId}")
    IdNaamBudget findById(@PathVariable long werknemerId){
        return werknemerService.findById(werknemerId)
                .map(werknemer -> new IdNaamBudget(werknemer))
                .orElseThrow(()-> new WerknemerNotFoundException(werknemerId));
    }*/

    @GetMapping("werknemers/{id}")
    Optional<Werknemer> findById(@PathVariable long id){
        return werknemerService.findById(id);
    }
}
