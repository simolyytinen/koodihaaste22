package com.solidabis.koodihaaste22;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestDataUtils {
    public static String getTestHtml(String resourceName) {
        var resource = Resources.getResource(resourceName);
        try {
            return Resources.toString(resource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("could not load resource " + resourceName, e);
        }
    }
}
