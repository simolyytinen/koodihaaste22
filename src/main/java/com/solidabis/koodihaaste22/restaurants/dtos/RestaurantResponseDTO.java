package com.solidabis.koodihaaste22.restaurants.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RestaurantResponseDTO {
    @Schema(description = "If non-null, contains the voted restaurant id for today")
    private String alreadyVoted;
    @Schema(description = "Current date")
    private String date;
    @Schema(description = "List of restaurants")
    private List<RestaurantDTO> restaurants;
}
