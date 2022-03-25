package com.solidabis.koodihaaste22.lounaspaikat.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RestaurantDTO {
    private String id;
    private String name;
    private String openingHours;
    private Integer votes;
    private List<DishDTO> dishes;
}
