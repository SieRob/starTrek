package be.vdab.star_trek.repositories;

import be.vdab.star_trek.domain.Werknemer;
import be.vdab.star_trek.exceptions.WerknemerNotFoundException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class WerknemerRepository {
    private final JdbcTemplate template;

    private final RowMapper<Werknemer> mapper = (rs, rowNum) ->
            new Werknemer(rs.getLong("id"),
                    rs.getString("voornaam"),
                    rs.getString("familienaam"),
                    rs.getBigDecimal("budget"));

    public WerknemerRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Werknemer> findAll() {
        var sql= """
                SELECT id, voornaam, familienaam, budget
                FROM werknemers
                ORDER BY voornaam
                """;

        return template.query(sql, mapper);
    }

    public Optional<Werknemer> findById(long id) {
        try {
            var sql = """
                    SELECT id, voornaam, familienaam, budget
                    FROM werknemers
                    WHERE id = ?
                    """;

            return Optional.of(template.queryForObject(sql, mapper, id));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }

    public void updateBudget(long id, BigDecimal budget) {
        var sql= """
                UPDATE werknemers
                SET budget = ?
                WHERE id = ?
                """;

        if(template.update(sql, budget, id)==0){
            throw new WerknemerNotFoundException(id);
        }
    }
}
