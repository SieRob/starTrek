"use strict";
import {byId, setText, toon, verberg} from "./util.js";
const werknemerId = JSON.parse(sessionStorage.getItem("werknemerId"));
if(!werknemerId){
    toon("noSessionid");
    window.location = "index.html";
}
findById()


async function findById() {
    const response1 = await fetch(`werknemers/${werknemerId}`);
    if (response1.ok) {
        const werknemer = await response1.json();

        setText("BestellingHeader", "Bestellingen van " + werknemer.voornaam + " " + werknemer.familienaam);
        setText("returnToWerknemer", werknemer.voornaam + " " + werknemer.familienaam);
    }
    const response2 = await fetch(`bestellingen/${werknemerId}`);
    if (response2.ok) {
        const bestellingen = await response2.json();
        const tableBody = byId("bestellingBody")
        for (const bestelling of bestellingen){
            const tr = tableBody.insertRow();
            tr.insertCell().innerText = bestelling.id;
            tr.insertCell().innerText = bestelling.omschrijving;
            tr.insertCell().innerText = bestelling.bedrag;
            tr.insertCell().innerText = new Date(bestelling.moment).toLocaleString("nl-BE");

        }

    } else {
        if (response2.status === 404) {
            toon("nietGevonden");
        } else {
            toon("storing");
        }
    }
}

function verbergFouten() {
    verberg("noSessionid");
}