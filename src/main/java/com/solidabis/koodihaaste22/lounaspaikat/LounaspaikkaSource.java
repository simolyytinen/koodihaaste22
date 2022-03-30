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
            var url = String.format("https://www.lounaat.info/haku?etsi=%s", city);
            return Jsoup.connect(url).get().toString();
        } catch (IOException e) {
            throw new RuntimeException("Ei voi ladata lounastietoja kaupungille", e);
        }
    }
}
