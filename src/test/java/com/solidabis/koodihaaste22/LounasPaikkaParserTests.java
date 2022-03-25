package com.solidabis.koodihaaste22;

import com.google.common.io.Resources;
import com.solidabis.koodihaaste22.lounaspaikat.LounaspaikkaParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LounasPaikkaParserTests {
    @Test
    public void shouldParseEmptyLounaspaikkaListFromEmptyDocument() {
        var parser = new LounaspaikkaParser();
        var lounasPaikat = parser.parse("<html></html>");
        assertEquals(0, lounasPaikat.size());
    }

    @Test
    public void shouldParseExampleRestaurants() throws IOException {
        var resource = Resources.getResource("kempele.html");
        var html = Resources.toString(resource, StandardCharsets.UTF_8);

        var parser = new LounaspaikkaParser();
        var lounasPaikat = parser.parse(html);
        assertEquals(3, lounasPaikat.size());
    }

}
