# Solidabis koodihaaste 2022

Tehtävänäsi on toteuttaa lounaspaikkaäänestyssovelluksen frontend valmista APIa vasten (työkalut saat valita itse).
Arvosteluperusteet tärkeysjärjestyksessä:

 1. Ratkaisun toimivuus
    1. ravintoloiden haku paikkakuntakohtaisesti
    2. äänen antaminen, muuttaminen ja poistaminen
    3. päiväkohtaisten äänestystulosten esittäminen
 2. Testit
 3. Ratkaisun selkeys ja yksinkertaisuus
 4. Käyttöliittymäratkaisut

Tässä repositoryssä on valmis backend, joka toteuttaa lounaspaikkojen
haku- ja äänestyslogiikan käyttäen Lounaat.info -palvelua.

Backendin ajamiseen tarvitset joko JDK:n ja/tai Dockerin asennettuna työasemallesi.

Backendin käynnistys:

    ./gradlew bootRun

Backend löytyy myös valmiiksi buildattuna konttina Docker Hubista, jolloin et tarvitse JDK-asennusta:

    docker run -p 8080:8080 solidabis/koodihaaste22:latest

Jos haluat buildata kontin itse:

    ./gradlew jibDockerBuild

Tutustu API-dokumentaatioon http://localhost:8080/swagger-ui.html

Päivä/selainkohtainen äänioikeus on toteutettu HTTP-only -cookiella.

Tuki: <sähköpostiosoite>
