package com.solidabis.koodihaaste22.restaurants;

import com.solidabis.koodihaaste22.restaurants.db.RestaurantRepository;
import com.solidabis.koodihaaste22.restaurants.parsing.Dish;
import com.solidabis.koodihaaste22.restaurants.parsing.Restaurant;
import com.solidabis.koodihaaste22.restaurants.parsing.RestaurantParser;
import com.solidabis.koodihaaste22.persistence.TodaysVoteRepository;
import com.solidabis.koodihaaste22.restaurants.dtos.DishDTO;
import com.solidabis.koodihaaste22.restaurants.dtos.RestaurantResponseDTO;
import com.solidabis.koodihaaste22.restaurants.dtos.RestaurantDTO;
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
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1", produces = "application/json")
public class RestaurantController {
    public static final int VOTERID_EXPIRY_TIME_SECONDS = 60 * 60 * 24;

    private final TodaysVoteRepository voteRepository;
    private final TimeSource timeSource;
    private final RestaurantParser parser;
    private final RestaurantSource source;
    private final RestaurantRepository repository;

    public RestaurantController(TodaysVoteRepository voteRepository, TimeSource timeSource, RestaurantParser parser,
                                RestaurantSource source, RestaurantRepository repository) {
        this.voteRepository = voteRepository;
        this.timeSource = timeSource;
        this.parser = parser;
        this.source = source;
        this.repository = repository;
    }

    @GetMapping("/restaurants/{city}")
    @Operation(summary = "Load restaurants for given city. Will return a cookie containing the voter id if not set for the request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant loading was successful"),
            @ApiResponse(responseCode = "500", description = "Could not fetch/parse restaurants from the source",
                    content = { @Content(schema = @Schema(description = "Default Spring error response", type = "object"))})
    })
    @Transactional
    public RestaurantResponseDTO fetchRestaurants(@CookieValue(name= Constants.VOTERID_COOKIE_NAME, required = false) String voterIdCookie,
                                                  @PathVariable("city") String city,
                                                  HttpServletResponse response) throws IOException {
        String voterId = makeOrReturnVoterCookie(voterIdCookie, response);

        String html = source.loadCity(city);
        var restaurants = parser.parse(html);
        restaurants.forEach(repository::saveRestaurant);
        var restaurantDTOs = restaurants.stream()
                // filter out places that are not actually in city, name could contain the city!
                .filter(restaurant -> restaurant.getCity().equalsIgnoreCase(city))
                .map(this::makeRestaurantDTO)
                .collect(Collectors.toList());

        return RestaurantResponseDTO.builder()
                .alreadyVoted(voteRepository.todaysVote(voterId, timeSource.today()))
                .date(timeSource.today().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .restaurants(restaurantDTOs)
                .build();
    }

    private String makeOrReturnVoterCookie(String voterIdCookie, HttpServletResponse response) {
        if(voterIdCookie != null) return voterIdCookie;
        var voterId = UUID.randomUUID().toString();
        var cookie = new Cookie(Constants.VOTERID_COOKIE_NAME, voterId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(VOTERID_EXPIRY_TIME_SECONDS);
        response.addCookie(cookie);
        return voterId;
    }

    private RestaurantDTO makeRestaurantDTO(Restaurant paikka) {
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
