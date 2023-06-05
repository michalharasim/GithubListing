package com.michalharasim.githublisting;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.michalharasim.githublisting.model.GithubBranch;
import com.michalharasim.githublisting.model.GithubRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@Service
public class GithubService {

    private boolean error;
    private final Gson gson = new Gson();
    private static final String address = "https://api.github.com";
    public ArrayList<GithubRepository> getRepositories(String username) {
        try {
            error = false;
            ArrayList<GithubRepository> allRepositories = new ArrayList<>();
            URL url = new URL(address + "/users/" + username + "/repos");
            String repoResponse = readSite(url);
            if (repoResponse == null || error) {
                return null;
            }
            JsonArray jsonArray = gson.fromJson(repoResponse, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.get("fork").getAsString().contains("false")) {
                    String name = jsonObject.get("name").getAsString();
                    String ownerLogin = jsonObject.get("owner").getAsJsonObject().get("login").getAsString();
                    ArrayList<GithubBranch> branches = getBranches(username, name);
                    GithubRepository githubRepository = new GithubRepository(name, ownerLogin, branches);
                    allRepositories.add(githubRepository);
                }
            }
            return allRepositories;
        } catch (IOException e) {
            System.out.println("ERROR HAPPENED INSIDE GETREPOSITORIES");
        }
        return null;
    }

    private ArrayList<GithubBranch> getBranches(String username, String repoName) throws IOException {
        try {
            error = false;
            ArrayList<GithubBranch> allBranches = new ArrayList<>();
            URL url = new URL(address + "/repos/" + username + "/" + repoName + "/branches");
            String branchResponse = readSite(url);
            JsonArray jsonArray = gson.fromJson(branchResponse, JsonArray.class);
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String branchName = jsonObject.get("name").getAsString();
                String sha = jsonObject.get("commit").getAsJsonObject().get("sha").getAsString();
                allBranches.add(new GithubBranch(branchName, sha));
            }
            return allBranches;
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private String readSite(URL url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            error = true;
        }
        return null;
    }
}
