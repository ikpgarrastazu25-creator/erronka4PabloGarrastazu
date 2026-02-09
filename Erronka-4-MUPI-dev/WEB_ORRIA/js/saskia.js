const produktuKontainer = document.getElementById('produktuak'); // section id="produktuak"
const kantitateaElement = document.getElementById('kantitatea');
const prezioaElement = document.getElementById('prezioa');
const hutsikMezua = document.getElementById('karritoa_hutsik');
const totalaKontainer = document.getElementById('totala');
const berrezarriBtn = document.getElementById('berrezarri');
const erosiBtn = document.getElementById('erosiBtn');

function sortuProduktuak() {
    produktuKontainer.innerHTML = "";
    const produktuak = JSON.parse(localStorage.getItem(keyLocalStorage));

    if (produktuak && produktuak.length > 0) {
        hutsikMezua.classList.add("izkutatu");
        hutsikMezua.style.display = "none";
        totalaKontainer.classList.remove("izkutatu");
        totalaKontainer.style.display = "block";

        produktuak.forEach(produktu => {
            const txartela = document.createElement("div");
            txartela.classList.add("saskia-produktua");
            txartela.style.border = "1px solid #ddd";
            txartela.style.padding = "10px";
            txartela.style.marginBottom = "10px";
            txartela.style.display = "flex";
            txartela.style.justifyContent = "space-between";
            txartela.style.alignItems = "center";

            txartela.innerHTML = `
                <div style="display: flex; align-items: center; gap: 10px;">
                    <img src="${produktu.img}" alt="${produktu.productName}" style="width: 50px; height: 50px; object-fit: cover;">
                    <div>
                        <h3>${produktu.productName}</h3>
                        <p>Prezioa: $${produktu.price}</p>
                    </div>
                </div>
                <div style="display: flex; align-items: center; gap: 10px;">
                    <button class="kendu-btn">-</button>
                    <span>${produktu.kantitatea}</span>
                    <button class="gehitu-btn">+</button>
                </div>
            `;

            const kenduBtn = txartela.querySelector(".kendu-btn");
            const gehituBtn = txartela.querySelector(".gehitu-btn");

            kenduBtn.addEventListener("click", () => {
                karritoariKendu(produktu);
                sortuProduktuak();
            });

            gehituBtn.addEventListener("click", () => {
                karritoraGehitu(produktu);
                sortuProduktuak();
            });

            produktuKontainer.appendChild(txartela);
        });
        eguneratuTotalak(produktuak);
        if (erosiBtn) erosiBtn.disabled = false;

    } else {
        hutsikMezua.classList.remove("izkutatu");
        hutsikMezua.style.display = "block";
        totalaKontainer.classList.add("izkutatu");
        totalaKontainer.style.display = "none";
        eguneratuTotalak([]);
        if (erosiBtn) erosiBtn.disabled = true;
    }
}

function eguneratuTotalak(produktuak) {
    const kantitatea = produktuak.reduce((acc, curr) => acc + curr.kantitatea, 0);
    const prezioa = produktuak.reduce((acc, curr) => acc + (curr.price * curr.kantitatea), 0);

    kantitateaElement.innerText = kantitatea;
    prezioaElement.innerText = prezioa.toFixed(2);
}

berrezarriBtn.addEventListener('click', () => {
    karritoaHustu();
    sortuProduktuak();
});

if (erosiBtn) {
    erosiBtn.addEventListener('click', () => {
        alert("Erosketa ondo burutu da! Eskerrik asko.");
        karritoaHustu();
        sortuProduktuak();
    });
}

// Hasieratu
sortuProduktuak();