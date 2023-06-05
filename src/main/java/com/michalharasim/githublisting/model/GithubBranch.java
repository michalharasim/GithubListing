package com.michalharasim.githublisting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GithubBranch {
    private String name;
    private String commitSha;
}
