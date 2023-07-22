package com.michalharasim.githublisting.controller;


import com.michalharasim.githublisting.service.GithubService;
import com.michalharasim.githublisting.model.GithubRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Getter
@Setter
public class GithubController {

    private GithubService githubService;

    @Autowired
    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @RequestMapping("/users/{username}/repos")
    public List<GithubRepository> getAllRepos(@PathVariable String username, @RequestHeader("Accept") String header) {
        return githubService.getRepositories(username, header);
    }
}
