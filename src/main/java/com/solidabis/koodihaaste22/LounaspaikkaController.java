package com.solidabis.koodihaaste22;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class LounaspaikkaController {
    private static final String VOTERID_COOKIE_NAME = "VOTERID";

    @GetMapping("/lounaspaikat/{city}")
    public LounasPaikkaResponseDTO haeLounasPaikat(@CookieValue(name=VOTERID_COOKIE_NAME, required = false) String voterIdCookie,
                                  @PathVariable("city") String city,
                                  HttpServletResponse response) {
        if(voterIdCookie==null) {
            // lähetä cookie
            var cookie = new Cookie(VOTERID_COOKIE_NAME, "432432432432");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        var dishes = List.of(
          DishDTO.builder()
                  .price("10,90EUR")
                  .name("Helmen lihapullaa ja ruskeaa kastiketta")
                  .attributes(List.of("L","G"))
                  .build()
        );
        var restaurants = List.of(
            RestaurantDTO.builder()
                    .id("9rewu9rewrew9u")
                    .name("Shell HelmiSimpukka Kempele")
                    .openingHours("10-14")
                    .votes(10)
                    .dishes(dishes)
                    .build()
        );
        return LounasPaikkaResponseDTO.builder()
                .alreadyVoted(false)
                .restaurants(restaurants)
                .build();
    }
}
