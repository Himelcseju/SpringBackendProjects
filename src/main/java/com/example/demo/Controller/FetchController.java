package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.ApiResponse;
import com.example.demo.Entity.LeaguesVo;
import com.example.demo.Service.LeagueService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class FetchController {

    private List<LeaguesVo> leagueList = new ArrayList<>();

    @Autowired
    private LeagueService leagueService;

    @GetMapping("/api/all")
    public List<LeaguesVo> getAllLeagues() {

        leagueList = leagueService.getAllLeagues();
        System.out.println(leagueList.toString());
        return leagueService.getAllLeagues();
    }

    @RequestMapping("/api")
    public String index() {
        ApiResponse response = new ApiResponse();
        response.setMessage("Welcome to APiWithSpringBoot");
        response.setStatus(true);

        return response.toString();
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping("/hello2")
    public String hello2() {
        return "Hello World 2!";
    }
}
