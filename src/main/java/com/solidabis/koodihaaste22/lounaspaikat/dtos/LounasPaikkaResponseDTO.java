package com.solidabis.koodihaaste22.lounaspaikat.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LounasPaikkaResponseDTO {
    @Schema(description = "If non-null, contains the voted restaurant id for today")
    private String alreadyVoted;
    @Schema(description = "List of restaurants")
    private List<RestaurantDTO> restaurants;
}
