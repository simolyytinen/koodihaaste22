package com.solidabis.koodihaaste22.voting;

import com.solidabis.koodihaaste22.voting.dtos.VotingResultDTO;
import com.solidabis.koodihaaste22.voting.dtos.RestaurantVotesDTO;
import com.solidabis.koodihaaste22.utils.Constants;
import com.solidabis.koodihaaste22.utils.TimeSource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1", produces = "application/json")
public class VotingController {
    private final VoteRepository voteRepository;
    private final TimeSource timeSource;

    public VotingController(VoteRepository voteRepository, TimeSource timeSource) {
        this.voteRepository = voteRepository;
        this.timeSource = timeSource;
    }

    @PostMapping("/vote/{restaurantid}")
    @Operation(summary = "Give/remove a vote for given restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote/vote removal was succesful"),
            @ApiResponse(responseCode = "400", description = "VOTERID cookie was missing"),
            @ApiResponse(responseCode = "500", description = "Database error occurred")
    })
    @Transactional
    public void registerVote(@PathVariable("restaurantid") String restaurantId,
                             @CookieValue(name = Constants.VOTERID_COOKIE_NAME) String voterIdCookie) {
        voteRepository.registerVote(restaurantId, voterIdCookie, timeSource.today());
    }

    @GetMapping("/results")
    @Operation(summary = "Return todays voting results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result retrieval was successful"),
            @ApiResponse(responseCode = "500", description = "Database error occurred")
    })
    @Transactional
    public VotingResultDTO results() {
        var results = voteRepository.getResults(timeSource.today());
        var resultList = results.stream().map(result -> RestaurantVotesDTO.builder()
                .votes(result.getVotes())
                .restaurantid(result.getRestaurantId())
                .name(result.getName())
                .city(result.getCity())
                .build());
        return VotingResultDTO.builder()
                .date(timeSource.today().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .results(resultList.collect(Collectors.toList()))
                .build();
    }
}
