const karritoZenbaketa = document.getElementById('zenbaketa');
const keyLocalStorage = "arropa";

document.addEventListener('DOMContentLoaded', () => {
    eguneratuZenbakia();
});

function karritoraGehitu(produktu) {
    let memoria = JSON.parse(localStorage.getItem(keyLocalStorage));
    let azkenKantitatea;

    if (!memoria || memoria.length === 0) {
        const produktuBerria = getProduktuBerriaMemoria(produktu);
        localStorage.setItem(keyLocalStorage, JSON.stringify([produktuBerria]));
        azkenKantitatea = 1;
    } else {
        const produktuIndizea = memoria.findIndex(arropa => arropa.id === produktu.id);
        const memoriaBerria = memoria;

        if (produktuIndizea === -1) {
            const produktuBerria = getProduktuBerriaMemoria(produktu);
            memoriaBerria.push(produktuBerria);
            azkenKantitatea = 1;
        } else {
            memoriaBerria[produktuIndizea].kantitatea++;
            azkenKantitatea = memoriaBerria[produktuIndizea].kantitatea;
        }
        localStorage.setItem(keyLocalStorage, JSON.stringify(memoriaBerria));

    }
    eguneratuZenbakia();
    return azkenKantitatea;
}

function karritoariKendu(produktu) {
    let memoria = JSON.parse(localStorage.getItem(keyLocalStorage));
    if (!memoria) return console.warn("ERROREA: Ez da karritoa aurkitu.");

    let azkenKantitatea = 0;
    const produktuIndizea = memoria.findIndex(arropa => arropa.id === produktu.id);

    if (produktuIndizea !== -1) {
        let memoriaBerria = memoria;
        memoriaBerria[produktuIndizea].kantitatea--;
        azkenKantitatea = memoriaBerria[produktuIndizea].kantitatea;

        if (azkenKantitatea === 0) {
            memoriaBerria.splice(produktuIndizea, 1);
        }
        localStorage.setItem(keyLocalStorage, JSON.stringify(memoriaBerria));
        eguneratuZenbakia();
    }
    return azkenKantitatea;
}

function getProduktuBerriaMemoria(produktu) {
    const produktuBerria = { ...produktu };
    produktuBerria.kantitatea = 1;
    return produktuBerria;
}

function eguneratuZenbakia() {
    let zenbaketa = 0;
    const memoria = JSON.parse(localStorage.getItem(keyLocalStorage));
    if (memoria && memoria.length > 0) {
        zenbaketa = memoria.reduce((acum, current) => acum + current.kantitatea, 0);
    }

    if (karritoZenbaketa) {
        karritoZenbaketa.innerText = zenbaketa;
    }
}

function karritoaHustu() {
    localStorage.removeItem(keyLocalStorage);
    eguneratuZenbakia();
}