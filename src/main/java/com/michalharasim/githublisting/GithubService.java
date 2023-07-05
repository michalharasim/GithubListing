package com.michalharasim.githublisting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michalharasim.githublisting.model.GithubBranch;
import com.michalharasim.githublisting.model.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class GithubService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String address = "https://api.github.com";


    public ArrayList<GithubRepository> getRepositories(String username) {
        try {
            ArrayList<GithubRepository> allRepositories = new ArrayList<>();
            String url = address + "/users/" + username + "/repos";
            String repoResponse = readSite(url);
            if (repoResponse == null) {
                return null;
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
            }
            return allRepositories;
        } catch (IOException e) {
            System.out.println("ERROR HAPPENED INSIDE GETREPOSITORIES");
            return null;
        }
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
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.out.println("No such user found.");
            return null;
        }
    }
}
