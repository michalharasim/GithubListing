package com.michalharasim.githublisting.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michalharasim.githublisting.exception.UserNotFoundException;
import com.michalharasim.githublisting.exception.XMLAsHeaderException;
import com.michalharasim.githublisting.model.GithubBranch;
import com.michalharasim.githublisting.model.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class GithubService {
    private WebClient webClient;
    private ObjectMapper objectMapper;
    private static final String address = "https://api.github.com";

    @Autowired
    public GithubService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public ArrayList<GithubRepository> getRepositories(String username, String header) {
        if (header.contains("application/xml")) {
            throw new XMLAsHeaderException();
        }
        if (header.contains("application/json")) {
            try {
                ArrayList<GithubRepository> allRepositories = new ArrayList<>();
                String url = address + "/users/" + username + "/repos";
                String repoResponse = readSite(url);
                if (repoResponse == null) {
                    throw new UserNotFoundException();
                }
                JsonNode jsonNode = objectMapper.readTree(repoResponse);
                if (jsonNode.isArray()) {
                    for (JsonNode node : jsonNode) {
                        String name = node.get("name").asText();
                        String ownerLogin = node.get("owner").get("login").asText();
                        ArrayList<GithubBranch> branches = getBranches(username, name);
                        GithubRepository githubRepository = new GithubRepository(name, ownerLogin, branches);
                        allRepositories.add(githubRepository);
                    }
                    return allRepositories;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    private ArrayList<GithubBranch> getBranches(String username, String repoName) throws IOException {
        ArrayList<GithubBranch> allBranches = new ArrayList<>();
        String url = address + "/repos/" + username + "/" + repoName + "/branches";
        String branchResponse = readSite(url);
        if (branchResponse == null) {
            return null;
        }
        JsonNode jsonNode = objectMapper.readTree(branchResponse);
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                String branchName = node.get("name").asText();
                String sha = node.get("commit").get("sha").asText();
                allBranches.add(new GithubBranch(branchName, sha));
            }
        }
        return allBranches;
    }

    private String readSite(String url) {
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> {
                        if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                            throw new UserNotFoundException();
                        }
                        return Mono.empty(); // mono.empty() - no exception is thrown, we continue with the application
                    })
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}
