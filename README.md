# Solidabis koodihaaste 2022

Tehtävänäsi on toteuttaa lounaspaikkaäänestyssovelluksen frontend valmista APIa vasten (työkalut saat valita itse).
Arvosteluperusteet tärkeysjärjestyksessä:

 1. Ratkaisun oikeellisuus
    1. ravintoloiden haku paikkakuntakohtaisesti
    2. äänen antaminen, muuttaminen ja poistaminen
    3. äänestystulosten esittäminen reaaliajassa
 2. Testit
 3. Ratkaisun selkeys ja yksinkertaisuus
 4. Käyttöliittymäratkaisut

Tässä repositoryssä on valmis Spring Bootilla toteutettu backend, joka toteuttaa lounaspaikkojen
haku- ja äänestyslogiikan käyttäen Lounaat.info -palvelua.

Backendin ajamiseen tarvitset JDK:n (versio>=11) ja/tai Dockerin asennettuna työasemallesi.

Backendin käynnistys:

    ./gradlew bootRun

tai Dockerilla:

    docker run -p 8080:8080 solidabis/koodihaaste22:latest

Tutustu API-dokumentaatioon http://localhost:8080/swagger-ui.html

Päivä/selainkohtainen äänioikeus on toteutettu HTTP-only -cookiella.

# Palautus

_Forkkaa tästä repositorystä oma julkinen ratkaisureposi_ ja lähetä linkki 31.5.2022 mennessä sähköpostilla osoitteeseen
koodihaaste@solidabis.com. Muokkaa README.md -tiedostoa siten, että siitä ilmenee vastauksen
tarkastelua helpottavat tiedot, kuten käyttämäsi teknologiat ja muutaman lauseen kuvaus tekemistäsi
ratkaisuista. Voit myös julkaista ratkaisusi esim. Herokuun, muista liittää linkki ja mahdolliset salasanat sähköpostiin!

Backendin muuttaminen esim. autentikoinnin toteuttamiseksi on sallittua.

Kerro samalla haluatko osallistua vain kilpailuun ja arvontaan, vai haluatko Solidabiksen
ottavan yhteyttä myös työtarjouksiin liittyen. Se ei tarkoita, että sinulle lähetettäisiin roskapostia, vaan nimensä
mukaisesti esimerkiksi kutsu työhaastatteluun. Voit halutessasi
osallistua koodihasteeseen myös ilman, että haluat ottaa palkintoa
vastaan tai osallistua arvontaan.
