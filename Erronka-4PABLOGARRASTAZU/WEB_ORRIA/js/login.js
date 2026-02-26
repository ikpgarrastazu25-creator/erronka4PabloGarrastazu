/* Login formaren kudeaketa */
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); /* Formularioa bidaltzea eta orria berritzea saihestu */

    const erabiltzailea = document.getElementById('username').value;
    const pasahitza = document.getElementById('password').value;
    const mezua = document.getElementById('loginMessage');

    /* Login egiaztapena (Behin-behineko kredentzialak) */
    if (erabiltzailea === "admin" && pasahitza === "1234") {
        /* Logina ondo egin bada */
        mezua.style.color = "#27ae60";
        mezua.innerText = "✅ Logina ondo egin da! Ongi etorri.";
        
        /* 2 segundo itxoin eta orrialde nagusira bidali erabiltzailea */
        setTimeout(() => {
            window.location.href = "index.html";
        }, 2000);

    } else {
        /* Logina gaizki egin bada */
        mezua.style.color = "#e74c3c";
        mezua.innerText = "❌ Erabiltzailea edo pasahitza okerra da.";
    }
});