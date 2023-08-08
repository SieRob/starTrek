INSERT INTO bestellingen(werknemerId, omschrijving, bedrag, moment)
VALUES  ((SELECT id FROM werknemers WHERE voornaam = 'testVoornaam'), 'testOmschrijving', 10, now()),
        ((SELECT id FROM werknemers WHERE voornaam = 'Rob'), 'Destiny Merch', 150, now());