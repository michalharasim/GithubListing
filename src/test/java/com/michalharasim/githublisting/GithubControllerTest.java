package com.michalharasim.githublisting;

import com.michalharasim.githublisting.controller.GithubController;
import com.michalharasim.githublisting.model.GithubBranch;
import com.michalharasim.githublisting.model.GithubRepository;
import com.michalharasim.githublisting.service.GithubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GithubController.class)
@ExtendWith(SpringExtension.class)
public class GithubControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private GithubService githubService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GithubController(githubService)).build();
    }

    @Test
    public void testGithubController_GetAllRepos_ShowsAllRepositories() throws Exception {
        // Arrange
        GithubBranch branch1 = new GithubBranch("branch1", "sha1");
        GithubBranch branch2 = new GithubBranch("branch2", "sha2");
        GithubRepository repository1 = new GithubRepository("repo1", "user1",
                Arrays.asList(branch1, branch2));

        GithubBranch branch3 = new GithubBranch("branch3", "sha3");
        GithubBranch branch4 = new GithubBranch("branch4", "sha4");
        GithubRepository repository2 = new GithubRepository("repo2", "user1",
                Arrays.asList(branch3, branch4));

        ArrayList<GithubRepository> repositories = new ArrayList<>();
        repositories.add(repository1);
        repositories.add(repository2);

        when(githubService.getRepositories(anyString(), anyString())).thenReturn(repositories);

        mockMvc.perform(get("/users/{username}/repos", "user1").header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"repositoryName\":\"repo1\",\"ownerLogin\":\"user1\",\"githubBranches\":[{\"name\":\"branch1\",\"commitSha\":\"sha1\"},{\"name\":\"branch2\",\"commitSha\":\"sha2\"}]},{\"repositoryName\":\"repo2\",\"ownerLogin\":\"user1\",\"githubBranches\":[{\"name\":\"branch3\",\"commitSha\":\"sha3\"},{\"name\":\"branch4\",\"commitSha\":\"sha4\"}]}]"));
    }

    @Test
    public void testGithubController_GetAllRepos_XMLHeaderShowsErrorMessage() throws Exception {
        // Arrange
        GithubBranch branch1 = new GithubBranch("branch1", "sha1");
        GithubBranch branch2 = new GithubBranch("branch2", "sha2");
        GithubRepository repository1 = new GithubRepository("repo1", "user1",
                Arrays.asList(branch1, branch2));

        ArrayList<GithubRepository> repositories = new ArrayList<>();
        repositories.add(repository1);

        when(githubService.getRepositories(anyString(), eq("application/xml;charset=UTF-8"))).thenReturn(repositories);

        MvcResult mvcResult = mockMvc.perform(get("/users/{username}/repos",
                        "user1").header("Accept", "application/xml;charset=UTF-8"))
                .andReturn();

        assertEquals("application/xml;charset=UTF-8", mvcResult.getResponse().getContentType());
    }

}