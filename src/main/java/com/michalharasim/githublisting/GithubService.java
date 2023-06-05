package com.michalharasim.githublisting;

import com.michalharasim.githublisting.model.GithubRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class GithubService {


    public ArrayList<GithubRepository> getRepositories(String username) {

    }
}
