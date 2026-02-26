/* --- 1. ALDAGAI GLOBALA --- */
let products = []; // Hemen gordeko ditugu JSON-etik kargatutako datuak

/* --- 2. FUNTZIO NAGUSIA: Txartelak marrazten dituena --- */
const displayProducts = (productsToShow) => {
  const shopContent = document.getElementById("shopContent");
  if (!shopContent) return; // Erroreak ekiditeko elementua ez badago
  
  shopContent.innerHTML = ""; // Garbitu aurreko edukia

  if (productsToShow.length === 0) {
    shopContent.innerHTML = "<p>Ez dago produkturik aukera honekin.</p>";
    return;
  }

  productsToShow.forEach(product => {
    const div = document.createElement("div");
    div.className = 'produktu-txartela';

    // Datu-basetik irudiak puntu eta komaz (;) banatuta datoz, lehenengoa hartuko dugu
    const lehenIrudia = product.irudiak ? product.irudiak.split(';')[0] : 'https://via.placeholder.com/150';

    div.innerHTML = `
      <img src="${lehenIrudia}" alt="${product.izena}" onerror="this.src='https://via.placeholder.com/150'"> 
      <h3>${product.izena}</h3>
      <div class="txartel-xehetasunak">
          <div class="prezio-kaxa">
             <p class="prezioa"> ${product.prezioa} €</p>
          </div>
          <div class="balorazioa">
             <span class="izarra">★</span> 4.5
          </div>
      </div>
      <div class="tailak">
         <span class="taila-etiketa">S</span>
         <span class="taila-etiketa">M</span>
         <span class="taila-etiketa">L</span>
         <span class="taila-etiketa">XL</span>
      </div>
      <button class="erosi-btn">Erosi</button>
    `;

    const erosiBotoia = div.querySelector('.erosi-btn');
    erosiBotoia.addEventListener('click', () => {
      if(typeof karritoraGehitu === 'function') {
         karritoraGehitu(product);
      }
      alert("Produktua saskira gehitu da!");
    });
    shopContent.append(div);
  });
};

/* --- 3. DOM ELEMENTUAK ETA IRAGAZKIAK --- */
const checkboxak = {
  kamisetak: document.getElementById('kamisetakBtn'),
  galtzak: document.getElementById('galtzakBtn'),
  zapatak: document.getElementById('zapatakBtn'),
  txaketak: document.getElementById('txaketakBtn'),
  jertseak: document.getElementById('jertseakBtn'),
  soinekoak: document.getElementById('soinekoakBtn'),
  zapatilak: document.getElementById('zapatilakBtn'),
  umeKamisetak: document.getElementById('umekamisetakBtn'),
  umeGaltzak: document.getElementById('umegaltzakBtn'),
  umeZapatilak: document.getElementById('umezapatilakBtn'),
};
const denakBtn = document.getElementById('denakBtn'); 
const denakumeBtn = document.getElementById('denakumeBtn'); 

/* --- 4. UPDATE PRODUCTS FUNTZIOA --- */
const updateProducts = () => {
  const kategoriaaktibatuak = [];

  if (checkboxak.kamisetak && checkboxak.kamisetak.checked) kategoriaaktibatuak.push('kamisetak');
  if (checkboxak.galtzak && checkboxak.galtzak.checked) kategoriaaktibatuak.push('galtzak');
  if (checkboxak.jertseak && checkboxak.jertseak.checked) kategoriaaktibatuak.push('jertseak');
  if (checkboxak.txaketak && checkboxak.txaketak.checked) kategoriaaktibatuak.push('txaketak');
  if (checkboxak.zapatak && checkboxak.zapatak.checked) kategoriaaktibatuak.push('zapatak');
  if (checkboxak.zapatilak && checkboxak.zapatilak.checked) kategoriaaktibatuak.push('zapatilak');
  if (checkboxak.soinekoak && checkboxak.soinekoak.checked) kategoriaaktibatuak.push('soinekoak');
  if (checkboxak.umeKamisetak && checkboxak.umeKamisetak.checked) kategoriaaktibatuak.push('umekamiseta');
  if (checkboxak.umeGaltzak && checkboxak.umeGaltzak.checked) kategoriaaktibatuak.push('umegaltza');
  if (checkboxak.umeZapatilak && checkboxak.umeZapatilak.checked) kategoriaaktibatuak.push('umezapatila');

  if (kategoriaaktibatuak.length === 0) {
    displayProducts(products);
    if (denakBtn) denakBtn.checked = true;
    if (denakumeBtn) denakumeBtn.checked = true;
  } else {
    // JSON berriko 'kategoria' eremua irakurtzen dugu
    const productsToShow = products.filter(product =>
      product.kategoria && kategoriaaktibatuak.includes(product.kategoria.toLowerCase())
    );
    displayProducts(productsToShow);
    if (denakBtn) denakBtn.checked = false;
    if (denakumeBtn) denakumeBtn.checked = false;
  }
};

/* --- 5. EVENT LISTENERAK (Botoiak entzuteko) --- */
Object.values(checkboxak).forEach(checkbox => {
  if (checkbox) { 
    checkbox.addEventListener('change', updateProducts);
  }
});

if (denakBtn) {
  denakBtn.addEventListener('change', (e) => {
    if (e.target.checked) {
      if (checkboxak.kamisetak) checkboxak.kamisetak.checked = false;
      if (checkboxak.galtzak) checkboxak.galtzak.checked = false;
      if (checkboxak.jertseak) checkboxak.jertseak.checked = false;
      if (checkboxak.txaketak) checkboxak.txaketak.checked = false;
      if (checkboxak.zapatilak) checkboxak.zapatilak.checked = false;
      if (checkboxak.soinekoak) checkboxak.soinekoak.checked = false;
      displayProducts(products);
    } else {
      updateProducts();
    }
  });
}

if (denakumeBtn) {
  denakumeBtn.addEventListener('change', (e) => {
    if (e.target.checked) {
      if (checkboxak.umeKamisetak) checkboxak.umeKamisetak.checked = false;
      if (checkboxak.umeGaltzak) checkboxak.umeGaltzak.checked = false;
      if (checkboxak.umeZapatilak) checkboxak.umeZapatilak.checked = false;
      displayProducts(products);
    } else {
      updateProducts();
    }
  });
}

/* --- 6. ORRIA ABIARAZTEKO FUNTZIOA --- */
function abiaraziOrrialdea() {
  if (window.location.pathname.includes("umeak.html")) {
    const umeenKategoriak = ['umekamiseta', 'umegaltza', 'umezapatila'];
    const umeenProduktuak = products.filter(product =>
      product.kategoria && umeenKategoriak.includes(product.kategoria.toLowerCase())
    );
    displayProducts(umeenProduktuak);
    if (denakumeBtn) denakumeBtn.checked = true;

  } else {
    displayProducts(products);
  }
}

/* --- 7. JSON-A FETCH BIDEZ KARGATU --- */
function kargatuProduktuak() {
  fetch('../../produktu_guztiak.json')
    .then(erantzuna => erantzuna.json())
    .then(datuak => {
      products = datuak; 
      abiaraziOrrialdea(); 
    })
    .catch(errorea => console.error("Errorea datuak kargatzean:", errorea));
}

/* --- 8. HASIERAKO KARGA (Orria kargatzean hasiko dena) --- */
document.addEventListener('DOMContentLoaded', () => {
  kargatuProduktuak();
});