package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leagues")  // Replace "user" with your actual table name if different
public class LeaguesVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "league_id")
    private int leagueId;

    @Column(name = "league_name", nullable = false, length = 255)
    private String leagueName;

    @Column(name = "league_short_name", length = 255)
    private String leagueShortName;

    @Column(name = "league_country", length = 255)
    private String leagueCountry;

    @Column(name = "league_season")
    private int leagueSeason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueShortName() {
        return leagueShortName;
    }

    public void setLeagueShortName(String leagueShortName) {
        this.leagueShortName = leagueShortName;
    }

    public String getLeagueCountry() {
        return leagueCountry;
    }

    public void setLeagueCountry(String leagueCountry) {
        this.leagueCountry = leagueCountry;
    }

    public int getLeagueSeason() {
        return leagueSeason;
    }

    public void setLeagueSeason(int leagueSeason) {
        this.leagueSeason = leagueSeason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
