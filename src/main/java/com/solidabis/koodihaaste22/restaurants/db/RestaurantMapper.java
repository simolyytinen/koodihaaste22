package com.solidabis.koodihaaste22.restaurants.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RestaurantMapper {
    @Insert({"INSERT INTO restaurant (id,name,city) VALUES (#{id}, #{name}, #{city})"})
    void insert(String id, String name, String city);

    @Update({"UPDATE restaurant SET name=#{name}, city=#{city} WHERE id=#{id}"})
    void update(String id, String name, String city);
}
