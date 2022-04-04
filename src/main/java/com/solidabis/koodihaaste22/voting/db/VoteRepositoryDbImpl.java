package com.solidabis.koodihaaste22.voting.db;

import com.solidabis.koodihaaste22.voting.VoteRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Repository
public class VoteRepositoryDbImpl implements VoteRepository {
    private final SqlSession session;

    public VoteRepositoryDbImpl(SqlSession session) {
        this.session = session;
    }

    @Override
    public void registerVote(String restaurantId, String voterId, LocalDate today) {
        var mapper = mapper();
        var alreadyVotedRestaurant = mapper.alreadyVoted(voterId, today);
        if(alreadyVotedRestaurant!=null) {
            mapper.deleteVote(alreadyVotedRestaurant, voterId, today);
            if(alreadyVotedRestaurant.equals(restaurantId)) return;
        }
        try {
            mapper.insertVote(restaurantId, voterId, today);
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant not found");
        }
    }

    @Override
    public int getVotes(String restaurantId, LocalDate today) {
        return mapper().loadVotes(restaurantId, today);
    }

    @Override
    public String todaysVote(String voterId, LocalDate today) {
        return mapper().alreadyVoted(voterId, today);
    }

    @Override
    public List<VotingResult> getResults(LocalDate today) {
        return mapper().loadDayVotes(today);
    }

    private VoteMapper mapper() { return session.getMapper(VoteMapper.class); }
}
