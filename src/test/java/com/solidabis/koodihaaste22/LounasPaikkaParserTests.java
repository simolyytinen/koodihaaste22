package com.solidabis.koodihaaste22;

import com.solidabis.koodihaaste22.lounaspaikat.LounaspaikkaParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LounasPaikkaParserTests {
    @Test
    public void shouldParseEmptyLounaspaikkaListFromEmptyDocument() {
        var parser = new LounaspaikkaParser();
        var lounasPaikat = parser.parse("<html></html>");
        assertEquals(0, lounasPaikat.size());
    }

}
