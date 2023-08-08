"use strict";
import {byId, setText, toon, verberg} from "./util.js";
const werknemerId = JSON.parse(sessionStorage.getItem("werknemerId"));
if(!werknemerId){
    toon("noSessionid");
    window.location = "index.html";
}
findById()


async function findById() {
    const response = await fetch(`werknemers/${werknemerId}`);
    if (response.ok) {
        const werknemer = await response.json();

        setText("werknemer", werknemer.voornaam + " " + werknemer.familienaam);
        setText("nummer", werknemer.id);
        setText("budget", werknemer.budget);


        const imgSpan = byId("image");
        const image = document.createElement("img");
        image.src = "images/" + werknemer.id + ".jpg";
        imgSpan.appendChild(image);

    } else {
        if (response.status === 404) {
            toon("nietGevonden");
        } else {
            toon("storing");
        }
    }
}

function verbergFouten() {
    verberg("noSessionid");
}