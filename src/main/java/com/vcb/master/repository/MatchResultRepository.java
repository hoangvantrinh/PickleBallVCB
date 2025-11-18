package com.vcb.master.repository;

import com.vcb.master.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
    List<MatchResult> findByPlayedAtBetween(Date start, Date end);
    @Query("SELECT k FROM MatchResult k WHERE  (k.playedAt between :fromDate and :toDate) AND (k.player1 =:playerName or k.player2 =:playerName or k.player3 =:playerName or k.player4 =:playerName)")
    List<MatchResult> findByData(
            @Param("fromDate")  Date fromDate, @Param("toDate")  Date toDate, @Param("playerName") String playerName);

}
