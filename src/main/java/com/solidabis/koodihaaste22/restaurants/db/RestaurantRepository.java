package com.solidabis.koodihaaste22.restaurants.db;

import com.solidabis.koodihaaste22.restaurants.parsing.Restaurant;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRepository {
    private final SqlSession session;

    public RestaurantRepository(SqlSession session) {
        this.session = session;
    }

    public void saveRestaurant(Restaurant restaurant) {
        var mapper = session.getMapper(RestaurantMapper.class);
        try {
            mapper.insert(restaurant.id(), restaurant.getName(), restaurant.getCity());
        } catch(Exception duplicateKey) {
            mapper.update(restaurant.id(), restaurant.getName(), restaurant.getCity());
        }
    }
}
