package com.solidabis.koodihaaste22.persistence;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository("dbimpl")
public class VoteRepositoryDbImpl implements VoteRepository {
    private final SqlSession session;

    public VoteRepositoryDbImpl(SqlSession session) {
        this.session = session;
    }

    @Override
    public void registerVote(String restaurantId, String voterIdCookie, LocalDate today) {
        var mapper = session.getMapper(VoteMapper.class);
        mapper.insertVote(restaurantId, voterIdCookie, today);
    }

    @Override
    public int getVotes(String restaurantId, LocalDate today) {
        var mapper = session.getMapper(VoteMapper.class);
        return mapper.loadVotes(restaurantId, today);
    }
}
