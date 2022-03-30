package com.solidabis.koodihaaste22.lounaspaikat;

import org.jsoup.Jsoup;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LounaspaikkaSource {
    @Cacheable("cities")
    public String loadCity(String city) {
        try {
            return Jsoup.connect("https://www.lounaat.info/haku?etsi="+city).get().toString();
        } catch (IOException e) {
            throw new RuntimeException("Ei voi ladata lounastietoja kaupungille "+city, e);
        }
    }
}
