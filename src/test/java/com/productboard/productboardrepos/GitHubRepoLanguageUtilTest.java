package com.productboard.productboardrepos;

import com.productboard.productboardrepos.domain.GitHubRepoInfo;
import com.productboard.productboardrepos.domain.LanguageInfo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GitHubRepoLanguageUtilTest {

    @Test
    void shouldConvertEmptyLanguageToRepoInfo() {
        // When
        String languages = "{}";
        GitHubRepoInfo gitHubRepoInfo = GitHubRepoLanguageUtil.convertToGitHugRepoInfo("repoName", languages);

        // Then
        assertThat(gitHubRepoInfo).isNotNull();
        assertThat(gitHubRepoInfo.repositoryName()).isEqualTo("repoName");

        List<LanguageInfo> languagesInfo = gitHubRepoInfo.languagesInfo();
        assertThat(languagesInfo.size()).isEqualTo(0);
    }

    @Test
    void shouldConvertNotEmptyLanguageToRepoInfo() {
        // When
        String languages = "{\"Ruby\":200,\"HTML\":300,\"Shell\":500}";
        GitHubRepoInfo gitHubRepoInfo = GitHubRepoLanguageUtil.convertToGitHugRepoInfo("repoName", languages);

        // Then
        assertThat(gitHubRepoInfo).isNotNull();
        assertThat(gitHubRepoInfo.repositoryName()).isEqualTo("repoName");

        List<LanguageInfo> languagesInfo = gitHubRepoInfo.languagesInfo();
        assertThat(languagesInfo.size()).isEqualTo(3);
        assertThat(languagesInfo.stream().map(LanguageInfo::language)).containsAll(List.of("Ruby", "HTML", "Shell"));
        assertThat(languagesInfo.stream().map(LanguageInfo::percentage))
                .containsAll(List.of(
                                new BigDecimal("20.00"),
                                new BigDecimal("30.00"),
                                new BigDecimal("50.00")
                        )
                );
    }

}