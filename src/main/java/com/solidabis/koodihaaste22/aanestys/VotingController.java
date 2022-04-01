package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.aanestys.dtos.DailyVotingResultDTO;
import com.solidabis.koodihaaste22.aanestys.dtos.ResultDTO;
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
public class VotingController {
    private final VoteRepository voteRepository;
    private final TimeSource timeSource;

    public VotingController(VoteRepository voteRepository, TimeSource timeSource) {
        this.voteRepository = voteRepository;
        this.timeSource = timeSource;
    }

    @PostMapping("/aanestys/{restaurantid}")
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

    @GetMapping("/tulokset")
    @Operation(summary = "Return todays voting results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result retrieval was successful"),
            @ApiResponse(responseCode = "500", description = "Database error occurred")
    })
    @Transactional
    public DailyVotingResultDTO results() {
        var results = voteRepository.getDayResults(timeSource.today());
        var resultList = results.stream().map(result -> ResultDTO.builder()
                .votes(result.getVotes())
                .restaurantid(result.getRestaurantId())
                .name(result.getName())
                .city(result.getCity())
                .build());
        return DailyVotingResultDTO.builder()
                .date(timeSource.today().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .results(resultList.collect(Collectors.toList()))
                .build();
    }
}
