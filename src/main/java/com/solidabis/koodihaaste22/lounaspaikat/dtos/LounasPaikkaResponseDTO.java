package com.solidabis.koodihaaste22.lounaspaikat.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LounasPaikkaResponseDTO {
    private String alreadyVoted;
    private List<RestaurantDTO> restaurants;
}
