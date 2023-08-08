"use strict";
import {byId} from "./util.js";

const response = await fetch("index");
if(response.ok){
    const werknemers = await response.json();
    const listBody= byId("werknemerList")
    for (const werknemer of werknemers){
        const li = document.createElement("li")
        const a = document.createElement("a")
        a.href = "werknemer.html";
        //a.dataset.werknemerId = werknemer.id
        a.innerText = werknemer.voornaam + " " + werknemer.familienaam;
        a.onclick = function (){sessionStorage.setItem("werknemerId", JSON.stringify(werknemer.id));};

        li.appendChild(a);
        listBody.appendChild(li);


    }

}