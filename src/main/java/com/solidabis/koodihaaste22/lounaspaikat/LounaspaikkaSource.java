package com.solidabis.koodihaaste22.lounaspaikat;

import org.jsoup.Jsoup;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.solidabis.koodihaaste22.utils.Constants.CITY_CACHE_NAME;

@Component
public class LounaspaikkaSource {
    @Cacheable(CITY_CACHE_NAME)
    public String loadCity(String city) throws IOException {
        var url = String.format("https://www.lounaat.info/haku?etsi=%s", city);
        return Jsoup.connect(url).get().toString();
    }
}
