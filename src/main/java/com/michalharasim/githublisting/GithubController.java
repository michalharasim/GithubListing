package com.michalharasim.githublisting;


import com.michalharasim.githublisting.model.GithubRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GithubController {
    private GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @RequestMapping("users/{username}/repos")
    public ResponseEntity<?> getAllRepos(@PathVariable String username,                                          @RequestHeader("Accept") String header) {
        if (header.equals(MediaType.APPLICATION_JSON_VALUE)) {
            ArrayList<GithubRepository> listOfRespositories = githubService.getRepositories(username);
            return ResponseEntity.ok(listOfRespositories);
        } else if (header.equals(MediaType.APPLICATION_XML_VALUE)) {
            return ResponseEntity.status(406).body(
                    "xml/application header"
            );
        } else {
            return ResponseEntity.badRequest().body(
                    "user not found"
            );
        }
    }


}
