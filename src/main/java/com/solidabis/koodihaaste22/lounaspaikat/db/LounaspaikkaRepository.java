package com.solidabis.koodihaaste22.lounaspaikat.db;

import com.solidabis.koodihaaste22.lounaspaikat.parsing.LounasPaikka;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class LounaspaikkaRepository {
    private final SqlSession session;

    public LounaspaikkaRepository(SqlSession session) {
        this.session = session;
    }

    public void saveRestaurant(LounasPaikka lounasPaikka) {
        var mapper = session.getMapper(RestaurantMapper.class);
        try {
            mapper.insert(lounasPaikka.id(), lounasPaikka.getName(), lounasPaikka.getCity());
        } catch(Exception duplicateKey) {
            mapper.update(lounasPaikka.id(), lounasPaikka.getName(), lounasPaikka.getCity());
        }
    }
}
