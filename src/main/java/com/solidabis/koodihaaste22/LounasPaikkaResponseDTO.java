package com.solidabis.koodihaaste22;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LounasPaikkaResponseDTO {
    private Boolean alreadyVoted;
    private List<RestaurantDTO> restaurants;
}
