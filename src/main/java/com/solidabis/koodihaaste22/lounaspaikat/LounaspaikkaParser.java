package com.solidabis.koodihaaste22.lounaspaikat;

import org.jsoup.Jsoup;

import java.util.List;
import java.util.stream.Collectors;

public class LounaspaikkaParser {
    public List<LounasPaikka> parse(String html) {
        var document = Jsoup.parse(html);
        var restaurantElements = document.select("div.menu.item");
        return restaurantElements.stream()
                .map(element -> LounasPaikka.builder().build())
                .collect(Collectors.toList());
    }
}
