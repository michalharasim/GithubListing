package com.michalharasim.githublisting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GithubRepository {

    private String repositoryName;
    private String ownerLogin;
    private List<GithubBranch> githubBranches;

    public GithubRepository(String repositoryName, String ownerLogin, List<GithubBranch> githubBranches) {
        this.repositoryName = repositoryName;
        this.ownerLogin = ownerLogin;
        this.githubBranches = githubBranches;
    }

}
