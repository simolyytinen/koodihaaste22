package com.solidabis.koodihaaste22.restaurants.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RestaurantDTO {
    @Schema(description = "Restaurant id to be used in voting requests. SHA256 of city and restaurant name")
    private String id;
    private String name;
    private String openingHours;
    @Schema(description = "Number of votes this restaurant has accumulated today")
    private Integer votes;
    private List<DishDTO> dishes;
}
