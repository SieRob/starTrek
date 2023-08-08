package be.vdab.star_trek.services;

import be.vdab.star_trek.domain.Bestellingen;
import be.vdab.star_trek.domain.NieuweBestelling;
import be.vdab.star_trek.dto.BestellingenMetNaam;
import be.vdab.star_trek.exceptions.OnvoldoendeBudgetException;
import be.vdab.star_trek.exceptions.WerknemerNotFoundException;
import be.vdab.star_trek.repositories.BestellingRepository;
import be.vdab.star_trek.repositories.WerknemerRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BestellingService {
    private BestellingRepository bestellingRepo;
    private WerknemerRepository werknemerRepo;

    public BestellingService(BestellingRepository bestellingRepo, WerknemerRepository werknemerRepo) {
        this.bestellingRepo = bestellingRepo;
        this.werknemerRepo = werknemerRepo;
    }

    public List<BestellingenMetNaam> findById(long id) {
        return bestellingRepo.findById(id);
    }

    @Transactional
    public long bestel(NieuweBestelling nieuweBestelling) {
        try{
            var id = nieuweBestelling.werknemerId();

            var bestelling = new Bestellingen(nieuweBestelling.werknemerId(), nieuweBestelling.omschrijving(), nieuweBestelling.bedrag());
            var werknemer = werknemerRepo.findById(id).orElseThrow(() -> new WerknemerNotFoundException(id));

            werknemer.bestel(nieuweBestelling.bedrag());
            werknemerRepo.updateBudget(id, werknemer.getBudget());
            return bestellingRepo.bestel(bestelling);

        }catch (DuplicateKeyException e){
            System.out.println(e);
            throw new OnvoldoendeBudgetException();
        }
    }
}
