package be.vdab.star_trek.repositories;

import be.vdab.star_trek.domain.Bestellingen;
import be.vdab.star_trek.domain.Werknemer;
import be.vdab.star_trek.dto.BestellingenMetNaam;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BestellingRepository {
    private final JdbcTemplate template;

    public BestellingRepository(JdbcTemplate template) {
        this.template = template;
    }


    public List<BestellingenMetNaam> findById(long id) {
        var sql = """
                SELECT b.id, omschrijving, bedrag, moment, w.voornaam, w.familienaam
                FROM bestellingen b
                LEFT JOIN werknemers w on b.werknemerId = w.id
                WHERE werknemerId = ?
                """;

        RowMapper<BestellingenMetNaam> mapper = (rs, rowNum) ->
                new BestellingenMetNaam(rs.getLong("id"),
                        rs.getString("omschrijving"),
                        rs.getBigDecimal("bedrag"),
                        rs.getObject("moment", LocalDateTime.class),
                        rs.getString("voornaam"),
                        rs.getString("familienaam"));

        return template.query(sql, mapper, id);

    }

    public long bestel(Bestellingen bestelling) {
        var sql = """
                INSERT INTO bestellingen(werknemerId, omschrijving, bedrag, moment)
                VALUES (?,?,?,?)
                """;

        var keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
                var stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setLong(1, bestelling.getWerknemerId());
                stmt.setString(2, bestelling.getOmschrijving());
                stmt.setBigDecimal(3, bestelling.getBedrag());
                stmt.setObject(4, bestelling.getMoment());
                return stmt;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
