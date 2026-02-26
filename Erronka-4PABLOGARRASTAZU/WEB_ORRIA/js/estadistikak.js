/* DOM-a kargatzean estatistikak eskatzen ditugu */
document.addEventListener('DOMContentLoaded', () => {
    fetch('esttistikak2.json')
        .then(erantzuna => erantzuna.json())
        .then(datuak => bistaratuEstatistikak(datuak))
        .catch(errorea => console.error("Errorea estatistikak kargatzean:", errorea));
});

/* JSON datuak HTML-an txertatzeko funtzioa */
function bistaratuEstatistikak(datuak) {
    /* 1. Irabazi guztizkoak */
    document.getElementById('irabazi-totala').innerText = datuak.irabaziak_guztira + " €";

    /* 2. Gutxieneko stocka */
    const stockBaxua = document.getElementById('stock-baxua');
    datuak.stock_baxuko_produktuak.forEach(p => {
        stockBaxua.innerHTML += `<li>${p.izena} <strong>(Stock: ${p.stocka})</strong></li>`;
    });

    /* 3. Gehien erositakoak */
    const gehienSalduak = document.getElementById('gehien-salduak');
    datuak.gehien_salduak.forEach(p => {
        gehienSalduak.innerHTML += `<li>${p.izena} - ${p.kopurua} unitate saldu dira</li>`;
    });

    /* 4. Bezero hoberenak */
    const bezeroOnenak = document.getElementById('bezero-onenak');
    datuak.bezero_onenak.forEach(b => {
        bezeroOnenak.innerHTML += `<li>${b.izena} - ${b.eskaerak} eskaera egin ditu</li>`;
    });

    /* 5. Hileko irabaziak */
    const hilekoIrabaziak = document.getElementById('hileko-irabaziak');
    datuak.hileko_irabaziak.forEach(h => {
        hilekoIrabaziak.innerHTML += `<li>${h.urtea}ko ${h.hila}: <strong>${h.irabazia} €</strong></li>`;
    });

    /* 6. Inoiz erosi ez direnak */
    const inoizErosiEz = document.getElementById('inoiz-erosi-ez');
    datuak.inoiz_erosi_ez_direnak.forEach(p => {
        inoizErosiEz.innerHTML += `<li>${p.izena}</li>`;
    });

    /* 7. 500€ baino gehiago */
    const errentagarriak = document.getElementById('errentagarriak');
    datuak.produktu_errentagarriak_500.forEach(p => {
        errentagarriak.innerHTML += `<li>${p.izena} - <strong>${p.irabazia} €</strong></li>`;
    });
}