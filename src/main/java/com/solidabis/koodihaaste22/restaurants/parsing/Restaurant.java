package com.solidabis.koodihaaste22.restaurants.parsing;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Restaurant {
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

    public String id() {
        return Hashing.sha256().hashString(city+name, Charsets.UTF_8).toString();
    }
}
