package com.michalharasim.githublisting;


import com.michalharasim.githublisting.exception.ApiRequestException;
import com.michalharasim.githublisting.exception.UserNotFoundException;
import com.michalharasim.githublisting.exception.XMLAsHeaderException;
import com.michalharasim.githublisting.model.GithubRepository;
import com.michalharasim.githublisting.service.GithubService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static reactor.core.publisher.Mono.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubServiceTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private GithubService githubService;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(githubService);
    }

    @Test
    public void testGithubService_GetAllRepositories_ValidUsername() {
        ArrayList<GithubRepository> repos = githubService.getRepositories("michalharasim", "application/json");
        assertThat(repos).isNotNull();
        assertThat(repos.stream().anyMatch(repo -> "michalharasim".equals(repo.getOwnerLogin()))).isTrue();
    }

    @Test
    public void testGithubService_GetAllRepositories_InvalidUsername() {
        assertThrows(UserNotFoundException.class, () -> {
            githubService.getRepositories("randomUsernameThatDoesntExist:[c]aw'",
                    "application/json");
        });
    }

    @Test
    public void testGithubService_GetAllRepositories_AcceptXMLHeader() {
        assertThrows(XMLAsHeaderException.class, () -> {
            githubService.getRepositories("randomUsernameThatDoesntExist:[c]aw'",
                    "application/xml");
        });
    }

    @Test
    public void testGithubService_GetAllRepositories_ApiErrorWhenRequestAPI() {
        assertThrows(ApiRequestException.class, () -> {
            githubService.getRepositories("randomUsernameThatDoesntExist:[c]aw'",
                    "applicatidawjdawidjaon/xml");
        });
    }




}
