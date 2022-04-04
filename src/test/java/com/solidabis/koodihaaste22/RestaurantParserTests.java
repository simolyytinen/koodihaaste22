package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.restaurants.parsing.RestaurantParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.solidabis.koodihaaste22.TestDataUtils.getTestHtml;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestaurantParserTests {

    private RestaurantParser parser;

    @BeforeEach
    public void setup() {
        parser = new RestaurantParser();
    }

    @Test
    public void shouldParseEmptyLounaspaikkaListFromEmptyDocument() {
        var lounasPaikat = parser.parse("<html></html>");
        assertEquals(0, lounasPaikat.size());
    }

    @Test
    public void shouldParseExampleRestaurants() {
        String html = getTestHtml("kempele.html");
        var lounasPaikat = parser.parse(html);
        assertEquals(3, lounasPaikat.size());
    }

    @Test
    public void shouldParseRestaurantProperties() {
        String html = getTestHtml("kempele.html");
        var paikat = parser.parse(html);
        assertEquals("Rosso Zeppelin, Kempele", paikat.get(0).getName());
        assertEquals("11-14", paikat.get(0).getOpeningHours());
        assertEquals("Kempele", paikat.get(0).getCity());
        assertEquals("Wieninleike l", paikat.get(0).getDishName(0));
        assertEquals(List.of("l"), paikat.get(0).getDishAttributes(0));
        assertEquals("13,90e", paikat.get(0).getDishPrice(0));
    }

}
