package com.michalharasim.githublisting.model;

import java.util.ArrayList;
import java.util.List;

public class GithubRepository {

    private String repositoryName;
    private String ownerLogin;
    private List<GithubBranch> githubBranches = new ArrayList<>();

    public GithubRepository(String repositoryName, String ownerLogin, List<GithubBranch> githubBranches) {
        this.repositoryName = repositoryName;
        this.ownerLogin = ownerLogin;
        this.githubBranches = githubBranches;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public List<GithubBranch> getGithubBranches() {
        return githubBranches;
    }

    public void setGithubBranches(List<GithubBranch> githubBranches) {
        this.githubBranches = githubBranches;
    }
}
