package com.vcb.master.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "MATCH_RESULT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player1;
    private String player2;
    private String player3;
    private String player4;

    private int scoreTeam1;
    private int scoreTeam2;

    private boolean isDoubles;
    private Date playedAt;

    public MatchResult(String player1, String player2, String player3, String player4, int scoreTeam1, int scoreTeam2, boolean isDoubles, Date playedAt) {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
        this.isDoubles = isDoubles;
        this.playedAt = playedAt;
    }
}
