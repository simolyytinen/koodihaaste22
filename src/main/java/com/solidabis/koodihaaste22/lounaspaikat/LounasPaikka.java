package com.solidabis.koodihaaste22.lounaspaikat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LounasPaikka {
    private String name;
    private String openingHours;
    private String city;
    private List<Dish> dishes;

    public String getDishName(int index) {
        return dishes.get(index).getName();
    }

    public List<String> getDishAttributes(int index) {
        return dishes.get(index).getAttributes();
    }

    public String getDishPrice(int index) {
        return dishes.get(index).getPrice();
    }
}
