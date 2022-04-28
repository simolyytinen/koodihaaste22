package com.solidabis.koodihaaste22;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RequestUtils {
    public static final String VOTERID_COOKIE_NAME = "VOTERID";
    public static final String GET_LOUNASPAIKAT_ENDPOINT = "/api/v1/restaurants/kempele";
    public static final String GET_RESULTS_ENDPOINT = "/api/v1/results";

    public static MockHttpServletRequestBuilder vote(String restaurantId, String voterId) {
        var cookie = new Cookie(VOTERID_COOKIE_NAME, voterId);
        return post(String.format("/api/v1/vote/%s", restaurantId)).cookie(cookie);
    }

    public static MockHttpServletRequestBuilder loadRestaurants(String voterId) {
        var cookie = new Cookie(VOTERID_COOKIE_NAME, voterId);
        return get(GET_LOUNASPAIKAT_ENDPOINT).cookie(cookie);
    }

    public static MockHttpServletRequestBuilder loadResults() {
        return get(GET_RESULTS_ENDPOINT);
    }
    public static MockHttpServletRequestBuilder loadResultsByDate(String date) {
        return get(String.format("/api/v1/results/%s", date));
    }
}
