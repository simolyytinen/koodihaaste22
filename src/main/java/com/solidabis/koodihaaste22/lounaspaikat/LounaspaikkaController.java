package com.solidabis.koodihaaste22.lounaspaikat;

import com.solidabis.koodihaaste22.lounaspaikat.parsing.Dish;
import com.solidabis.koodihaaste22.lounaspaikat.parsing.LounasPaikka;
import com.solidabis.koodihaaste22.lounaspaikat.parsing.LounaspaikkaParser;
import com.solidabis.koodihaaste22.persistence.VoteRepository;
import com.solidabis.koodihaaste22.lounaspaikat.dtos.DishDTO;
import com.solidabis.koodihaaste22.lounaspaikat.dtos.LounasPaikkaResponseDTO;
import com.solidabis.koodihaaste22.lounaspaikat.dtos.RestaurantDTO;
import com.solidabis.koodihaaste22.utils.Constants;
import com.solidabis.koodihaaste22.utils.TimeSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
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
    @Operation(summary = "Load restaurants for given city. Will return a cookie containing the voter id if not set for the request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant loading was successful"),
            @ApiResponse(responseCode = "500", description = "Could not fetch/parse restaurants from the source",
                    content = { @Content(schema = @Schema(description = "Default Spring error response", type = "object"))})
    })
    @Transactional
    public LounasPaikkaResponseDTO haeLounasPaikat(@CookieValue(name= Constants.VOTERID_COOKIE_NAME, required = false) String voterIdCookie,
                                                   @PathVariable("city") String city,
                                                   HttpServletResponse response) throws IOException {
        String voterId = makeOrReturnVoterCookie(voterIdCookie, response);

        String html = source.loadCity(city);
        var paikat = parser.parse(html);
        var ravintolat = paikat.stream()
                // filter out places that are not actually in city, name could contain the city!
                .filter(paikka -> paikka.getCity().equals(city))
                .map(this::makeRestaurantDTO)
                .collect(Collectors.toList());

        return LounasPaikkaResponseDTO.builder()
                .alreadyVoted(voteRepository.todaysVote(voterId, timeSource.today()))
                .restaurants(ravintolat)
                .build();
    }

    private String makeOrReturnVoterCookie(String voterIdCookie, HttpServletResponse response) {
        if(voterIdCookie != null) return voterIdCookie;
        var voterId = UUID.randomUUID().toString();
        var cookie = new Cookie(Constants.VOTERID_COOKIE_NAME, voterId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);
        return voterId;
    }

    private RestaurantDTO makeRestaurantDTO(LounasPaikka paikka) {
        var paikkaId = paikka.id();
        return RestaurantDTO.builder()
                .id(paikkaId)
                .name(paikka.getName())
                .openingHours(paikka.getOpeningHours())
                .votes(voteRepository.getVotes(paikkaId, timeSource.today()))
                .dishes(paikka.getDishes().stream().map(this::buildDishDTO).collect(Collectors.toList()))
                .build();
    }

    private DishDTO buildDishDTO(Dish dish) {
        return DishDTO.builder()
                .name(dish.getName())
                .price(dish.getPrice())
                .attributes(dish.getAttributes())
                .build();
    }
}
