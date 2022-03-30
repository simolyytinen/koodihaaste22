package com.solidabis.koodihaaste22.aanestys;

import com.solidabis.koodihaaste22.persistence.VoteRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class VoteRepositoryDbImpl implements VoteRepository {
    private final SqlSession session;

    public VoteRepositoryDbImpl(SqlSession session) {
        this.session = session;
    }

    @Override
    public void registerVote(String restaurantId, String voterIdCookie, LocalDate today) {
        var mapper = session.getMapper(VoteMapper.class);
        var alreadyVotedRestaurant = mapper.alreadyVoted(voterIdCookie, today);
        if(alreadyVotedRestaurant!=null) {
            mapper.deleteVote(alreadyVotedRestaurant, voterIdCookie, today);
            if(alreadyVotedRestaurant.equals(restaurantId)) return;
        }
        mapper.insertVote(restaurantId, voterIdCookie, today);
    }

    @Override
    public int getVotes(String restaurantId, LocalDate today) {
        var mapper = session.getMapper(VoteMapper.class);
        return mapper.loadVotes(restaurantId, today);
    }
}
