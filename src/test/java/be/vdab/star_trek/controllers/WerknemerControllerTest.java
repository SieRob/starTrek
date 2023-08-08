package be.vdab.star_trek.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql({"/werknemers.sql", "/bestellingen.sql"})
@AutoConfigureMockMvc
class WerknemerControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String WERKNEMERS = "werknemers";
    private final static String BESTELLINGEN = "bestellingen";
    private final static Path TEST_RESOURCES = Path.of("src/test/resources");
    private final MockMvc mockMvc; // constructor met parameter (dependency injection)

    public WerknemerControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /** TESTEN WERKEN NIET, Gekopieerd uit de curses maar niet aangepast naar mijn eigen code*/
    @Test
    void findAll() throws Exception {
        var aantalWerknemers = countRowsInTable(WERKNEMERS);
        mockMvc.perform(get("/index")).andExpectAll(status().isOk(), jsonPath("length()").value(aantalWerknemers));
    }

    private long idVanTestWerknemer() {
        return jdbcTemplate.queryForObject("select id from werknemers where voornaam = 'testVoornaam'", Long.class);
    }

    @Test
    void findBestellingenVan() throws Exception {
        var id = idVanTestWerknemer();
        var aantalBestellingen = countRowsInTableWhere(BESTELLINGEN, "werknemerId = " + id);
        mockMvc.perform(get("/bestellingen/{id}", id)).andExpectAll(status().isOk(), jsonPath("length()").value(aantalBestellingen));
    }

    @Test
    void bestel() throws Exception {
        //var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteBestelling.json"));
        var id = idVanTestWerknemer();
        System.out.println(id);
        var json = "{\"werknemerId\":"+ id + ", \"omschrijving\": \"testOmschrijving\", \"bedrag\": 80}";
        System.out.println(json);
        mockMvc.perform(post("/bestellingen").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
        assertThat(countRowsInTableWhere(BESTELLINGEN, "omschrijving = 'testOmschrijving' and bedrag = 80 and werknemerId =" + id)).isOne();
        assertThat(countRowsInTableWhere(WERKNEMERS, "budget = 20 and id = " + id)).isOne();
    }


    /**JSON data gaat moeilijk op te maken zijn door waardes van NieuweBestelling die Id mee moet krijgen*/
    /**Test slaagt omdat json files zelfs zonder de fouten bad request zijn (Hebben geen werknemerId)*/
    //@ParameterizedTest
    //@ValueSource(strings = {"bestellingZonderOmschrijving.json", "bestellingMetLegeOmschrijving.json", "bestellingZonderBedrag.json", "bestellingMetNegatiefBedrag.json"})
    @Test
    void bestelMetVerkeerdeDataMislukt() throws Exception {
        //var jsonData = Files.readString(TEST_RESOURCES.resolve(bestandsnaam));
        var id = idVanTestWerknemer();

        var bestellingZonderOmschrijving = "{\"werknemerId\":"+ id + ", \"bedrag\": 1}";
        var bestellingMetLegeOmschrijving = "{\"werknemerId\":"+ id + ", \"omschrijving\": \"\", \"bedrag\": 1}";
        var bestellingZonderBedrag = "{\"werknemerId\":"+ id + ", \"omschrijving\": \"testOmschrijving\"}";
        var bestellingMetNegatiefBedrag = "{\"werknemerId\":"+ id + ", \"omschrijving\": \"testOmschrijving\", \"bedrag\": -1}";

        mockMvc.perform(post("/bestellingen").contentType(MediaType.APPLICATION_JSON).content(bestellingZonderOmschrijving)).andExpect(status().isBadRequest());
        mockMvc.perform(post("/bestellingen").contentType(MediaType.APPLICATION_JSON).content(bestellingMetLegeOmschrijving)).andExpect(status().isBadRequest());
        mockMvc.perform(post("/bestellingen").contentType(MediaType.APPLICATION_JSON).content(bestellingZonderBedrag)).andExpect(status().isBadRequest());
        mockMvc.perform(post("/bestellingen").contentType(MediaType.APPLICATION_JSON).content(bestellingMetNegatiefBedrag)).andExpect(status().isBadRequest());
    }

    @Test
    void bestelVoorOnbestaandeWerknemerMislukt() throws Exception {
        //var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteBestelling.json"));

        var json = "{\"werknemerId\":"+ Long.MAX_VALUE + ", \"omschrijving\": \"testOmschrijving\", \"bedrag\": 10000}";
        mockMvc.perform(post("/bestellingen").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound());
    }

    @Test
    void bestelMetTeGrootBedragMislukt() throws Exception {
        //var jsonData = Files.readString(TEST_RESOURCES.resolve("bestellingMetTeGrootBedrag.json"));
        var id = idVanTestWerknemer();

        var json = "{\"werknemerId\":"+ id + ", \"omschrijving\": \"testOmschrijving\", \"bedrag\": 10000}";
        mockMvc.perform(post("/bestellingen").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isConflict());
    }
}