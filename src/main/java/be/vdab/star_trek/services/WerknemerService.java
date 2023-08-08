package be.vdab.star_trek.services;

import be.vdab.star_trek.domain.Werknemer;
import be.vdab.star_trek.repositories.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class WerknemerService {
    private WerknemerRepository werknemerRepo;

    public WerknemerService(WerknemerRepository werknemerRepo) {
        this.werknemerRepo = werknemerRepo;
    }

    public List<Werknemer> findAll() {
        return werknemerRepo.findAll();
    }

    public Optional<Werknemer> findById(long id) {
        return werknemerRepo.findById(id);
    }
}
