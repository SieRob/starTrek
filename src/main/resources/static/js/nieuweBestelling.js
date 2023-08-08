"use strict";
import {byId, setText, toon, verberg} from "./util.js";

const werknemerId = JSON.parse(sessionStorage.getItem("werknemerId"));
const response1 = await fetch(`werknemers/${werknemerId}`);
if(!werknemerId){
    toon("noSessionid");
    window.location = "index.html";
}
let budget = 0;

if (response1.ok) {
    const werknemer = await response1.json();
    setText("BestellingHeader", "Bestellingen voor " + werknemer.voornaam + " " + werknemer.familienaam);
    setText("returnToWerknemer", werknemer.voornaam + " " + werknemer.familienaam);
    budget = werknemer.budget
}
byId("bestel").onclick = async function () {
    verbergFouten();
    const input1 = byId("omschrijving");
    if (!input1.checkValidity()) {
        toon("omschrijvingFout")
        input1.focus();
        return;
    }

    const input2 = byId("bedrag");
    if (!input2.checkValidity()) {
        toon("bedragFout")
        input2.focus();
        return;
    } else if (input2.value > budget) {
        toon("onvoldoendeBudget")
        input2.focus();
        return;
    }
    const bestelling = {werknemerId: werknemerId, omschrijving: input1.value, bedrag: input2.value}

    voegToe(bestelling);
}


function verbergFouten() {
    verberg("omschrijvingFout");
    verberg("noSessionid");
    verberg("bedragFout");
    verberg("onvoldoendeBudget")
    verberg("storing");
    verberg("conflict");
}

async function voegToe(bestelling) {
    const resp = await fetch(`bestellingen`, {
        method: "POST",
        headers: {'Content-Type': "application/json"},
        body: JSON.stringify(bestelling)
    });
    if (resp.ok) {
        window.location = "vorigeBestellingen.html"
    } else {
        if (resp.status === 409) {
            const responseBody = await resp.json();
            setText("conflict", responseBody.message);
            toon("conflict");
        } else {
            toon("storing");
        }
    }
}