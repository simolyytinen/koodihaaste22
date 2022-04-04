package com.solidabis.koodihaaste22.voting.db;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface VoteMapper {
    @Insert({"INSERT INTO vote (restaurantid, voterid, votingdate) VALUES (#{restaurantId}, #{voterId}, #{today})"})
    void insertVote(String restaurantId, String voterId, LocalDate today);

    @Select({"SELECT COUNT(restaurantid) FROM vote WHERE restaurantid = #{restaurantId} AND votingdate = #{today}"})
    int loadVotes(String restaurantId, LocalDate today);

    @Select({"SELECT restaurantid FROM vote WHERE voterid = #{voterId} AND votingdate = #{today}"})
    String alreadyVoted(String voterId, LocalDate today);

    @Delete({"DELETE FROM vote WHERE restaurantid = #{restaurantId} AND voterid = #{voterId} AND votingdate = #{today}"})
    void deleteVote(String restaurantId, String voterId, LocalDate today);

    @Select({"SELECT COUNT(v.restaurantid) AS votes, v.restaurantid AS restaurantId, r.name AS name, r.city AS city",
            "FROM vote v",
            "INNER JOIN restaurant r ON v.restaurantid=r.id",
            "WHERE v.votingdate = #{today}",
            "GROUP BY v.restaurantid",
            "ORDER BY votes DESC"})
    List<VotingResult> loadDayVotes(LocalDate today);
}
