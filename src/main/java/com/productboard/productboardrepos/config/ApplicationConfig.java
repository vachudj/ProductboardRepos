package com.productboard.productboardrepos.config;

import com.productboard.productboardrepos.common.GitHubRepoTask;
import com.productboard.productboardrepos.github.ClientConfiguration;
import com.productboard.productboardrepos.github.GithubClient;
import com.productboard.productboardrepos.repository.LanguagesRepository;
import com.productboard.productboardrepos.scheduler.RefreshLanguagesJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@EnableScheduling
@Import(RepositoryConfig.class)
public class ApplicationConfig {

    @Bean
    ClientConfiguration retryConfiguration(@Value("${client.retry.attempts:12}") int retryAttempts,
                                           @Value("${client.retry.delaySec:5}") int retryDelay,
                                           @Value("${client.request.timeoutSec:10}") int requestDuration,
                                           @Value("${client.api.token}") String apiToken) {
        return new ClientConfiguration(retryAttempts,
                Duration.ofSeconds(retryDelay),
                Duration.ofSeconds(requestDuration),
                apiToken
        );
    }

    @Bean
    public GithubClient githubClient(ClientConfiguration clientConfiguration) {
        return new GithubClient(
                HttpClient.newHttpClient(),
                clientConfiguration
        );
    }

    @Bean
    public GitHubRepoTask gitRepositoriesTask(
            GithubClient githubClient,
            LanguagesRepository languagesRepository,
            @Value("${github.repository.all.url.template}") String gitRepositoryUrlTemplate,
            @Value("${client.request.itemPerPage:10}") int initLimit) {
        return new GitHubRepoTask(githubClient, languagesRepository, gitRepositoryUrlTemplate, initLimit);
    }

    @Bean
    public RefreshLanguagesJob refreshLanguagesJob(GitHubRepoTask gitHubRepoTask) {
        return new RefreshLanguagesJob(gitHubRepoTask);
    }
}
