package com.michalharasim.githublisting;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.michalharasim.githublisting.model.ErrorResponse;
import com.michalharasim.githublisting.model.GithubRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Getter
@Setter
public class GithubController {
    private GithubService githubService;
    private ObjectMapper objectMapper;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
        objectMapper = new ObjectMapper();
    }

    @RequestMapping("/users/{username}/repos")
    public ResponseEntity<?> getAllRepos(@PathVariable String username,                                          @RequestHeader("Accept") String header) throws IOException {
        System.out.println(username);
        System.out.println(header);
        if (header.contains(MediaType.APPLICATION_JSON_VALUE)) {
            ArrayList<GithubRepository> listOfRepositories = githubService.getRepositories(username);
            if (listOfRepositories == null) {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found.");
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
            }
            return ResponseEntity.ok(listOfRepositories);
        } else if (header.equals(MediaType.APPLICATION_XML_VALUE)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Header was given in " + header);
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error" + header);
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
        }
    }
}
