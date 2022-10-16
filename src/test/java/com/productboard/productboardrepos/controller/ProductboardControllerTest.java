package com.productboard.productboardrepos.controller;

import com.productboard.productboardrepos.domain.GitHubRepoInfo;
import com.productboard.productboardrepos.domain.LanguageInfo;
import com.productboard.productboardrepos.repository.LanguagesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductboardController.class)
class ProductboardControllerTest {

    @MockBean
    private LanguagesRepository languagesRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSpecificGitHubRepoInfo() throws Exception {
        String gitHubRepoName = "ruby-server-sdk";
        GitHubRepoInfo gitHubRepoInfo = createGitHubRepoInfor(gitHubRepoName, "java", BigDecimal.valueOf(100));

        Mockito.when(languagesRepository.getIfPresent(gitHubRepoName)).thenReturn(gitHubRepoInfo);

        mockMvc.perform(get("/api/percentage/percentage/" + gitHubRepoName)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"repositoryName\":\"ruby-server-sdk\",\"languagesInfo\":[{\"language\":\"java\",\"percentage\":100}]}")
        );
    }

    @Test
    void shouldReturnAllGitHubRepoInfo() throws Exception {
        String gitHubRepoName1 = "ruby-server-sdk";
        String gitHubRepoName2 = "freemail";
        GitHubRepoInfo gitHubRepoInfo1 = createGitHubRepoInfor(gitHubRepoName1, "java", BigDecimal.valueOf(100));
        GitHubRepoInfo gitHubRepoInfo2 = createGitHubRepoInfor(gitHubRepoName2, "Shell", BigDecimal.valueOf(100));

        Mockito.when(languagesRepository.getAll()).thenReturn(List.of(gitHubRepoInfo1, gitHubRepoInfo2));

        mockMvc.perform(get("/api/percentage/percentage/all")
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"repositoryName\":\"ruby-server-sdk\",\"languagesInfo\":[{\"language\":\"java\",\"percentage\":100}]}," +
                        "{\"repositoryName\":\"freemail\",\"languagesInfo\":[{\"language\":\"Shell\",\"percentage\":100}]}]"
                )
        );
    }

    private GitHubRepoInfo createGitHubRepoInfor(String repoName, String languageName, BigDecimal percentage) {
        return new GitHubRepoInfo(repoName, List.of(new LanguageInfo(languageName, percentage)));
    }
}
