package com.solidabis.koodihaaste22.persistence;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface VoteMapper {
    @Insert({"INSERT INTO vote (restaurantid, voterid, votingdate) VALUES (#{restaurantId}, #{voterId}, #{today})"})
    void insertVote(String restaurantId, String voterId, LocalDate today);

    @Select({"SELECT COUNT(restaurantid) FROM vote WHERE restaurantid = #{restaurantId} AND votingdate = #{today}"})
    int loadVotes(String restaurantId, LocalDate today);
}
