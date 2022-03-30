package com.solidabis.koodihaaste22.lounaspaikat;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.solidabis.koodihaaste22.persistence.VoteRepository;
import com.solidabis.koodihaaste22.lounaspaikat.dtos.DishDTO;
import com.solidabis.koodihaaste22.lounaspaikat.dtos.LounasPaikkaResponseDTO;
import com.solidabis.koodihaaste22.lounaspaikat.dtos.RestaurantDTO;
import com.solidabis.koodihaaste22.utils.Constants;
import com.solidabis.koodihaaste22.utils.TimeSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = "application/json")
public class LounaspaikkaController {
    private final VoteRepository voteRepository;
    private final TimeSource timeSource;
    private final LounaspaikkaParser parser;
    private final LounaspaikkaSource source;

    public LounaspaikkaController(VoteRepository voteRepository, TimeSource timeSource, LounaspaikkaParser parser,
                                  LounaspaikkaSource source) {
        this.voteRepository = voteRepository;
        this.timeSource = timeSource;
        this.parser = parser;
        this.source = source;
    }

    @GetMapping("/lounaspaikat/{city}")
    @Operation(summary = "Load restaurants for given city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant loading was successful")
    })
    @Transactional
    public LounasPaikkaResponseDTO haeLounasPaikat(@CookieValue(name= Constants.VOTERID_COOKIE_NAME, required = false) String voterIdCookie,
                                                   @PathVariable("city") String city,
                                                   HttpServletResponse response) {
        if(voterIdCookie==null) {
            // lähetä cookie
            var cookie = new Cookie(Constants.VOTERID_COOKIE_NAME, "432432432432");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        String html = source.loadCity(city);
        var paikat = parser.parse(html);
        var ravintolat = paikat.stream().map(paikka -> {
            var paikkaId = Hashing.sha256().hashString(paikka.getCity()+paikka.getName(), Charsets.UTF_8).toString();
            return RestaurantDTO.builder()
                    .id(paikkaId)
                    .name(paikka.getName())
                    .openingHours(paikka.getOpeningHours())
                    .votes(voteRepository.getVotes(paikkaId, timeSource.today()))
                    .dishes(paikka.getDishes().stream().map(dish ->
                        DishDTO.builder()
                                .name(dish.getName())
                                .price(dish.getPrice())
                                .attributes(dish.getAttributes())
                                .build()
                    ).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());

        return LounasPaikkaResponseDTO.builder()
                .alreadyVoted(false)
                .restaurants(ravintolat)
                .build();
    }
}
