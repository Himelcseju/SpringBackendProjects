package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.LeaguesVo;
import com.example.demo.Repository.LeaguesRepository;

@Service
public class LeagueService {

    @Autowired
    private LeaguesRepository leagueRepository;

    public List<LeaguesVo> getAllLeagues() {
        return leagueRepository.findAll();
    }

    // public LeaguesVo getAllLeaguesById(Integer id) {
    //     return leagueRepository.findById(id);
    // }
    // public LeaguesVo getAllLeaguesByName(String name) {
    //     return leagueRepository.findByName(name);
    // }
}
