# Solidabis koodihaaste 2022

# Frontend-toteutus

Tein tämän koodihaasteen varsinaisen palautusajan jälkeen heinäkuussa 2022. Toteutukseni sisältää tähän mennessä oppimiani asioita, joten kaikkea mahdollista ei ole huomioitu.

Frontend on toteutettu React JS:llä. Frontendin kehitysympäristön saa käyntiin /client hakemiston sisällä komennolla:

    npm start (ensimmäisellä kerralla myös "npm install")

Käyttöliittymä on yksinkertainen, ainoastaan pääsivu jossa on hakukenttä kaupungille sekä hakutulokset esitettynä ruudukkona. Pääsivulta on mahdollista äänestää haluttua ravintolaa. Äänestysnappien tekstit vaihtuvat riippuen siitä onko jo annettu ääni kyseisenä päivänä. Tulokset näkyvät reaaliajassa jokaisen ravintolan kohdalla. Erillistä sivua tulosten esittämiseen en luonut. En myöskään luonut erillistä sivua tietyn päivän tuloksien esittämiseen.

Tähän toteutukseen ei sisälly testejä, koska niiden kirjoittamiseen en ole vielä tutustunut.

Backendiä en muokannut ollenkaan.

# Alkuperäinen tehtävänanto

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
