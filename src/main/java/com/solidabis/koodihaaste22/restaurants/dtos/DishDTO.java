package com.solidabis.koodihaaste22.restaurants.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DishDTO {
    private String name;
    private String price;
    @Schema(description = "Dish attributes (lactose free, gluten free etc)")
    private List<String> attributes;
}
