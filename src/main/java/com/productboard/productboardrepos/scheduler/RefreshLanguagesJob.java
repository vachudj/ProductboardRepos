package com.productboard.productboardrepos.scheduler;

import com.productboard.productboardrepos.common.GitHubRepoTask;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

public class RefreshLanguagesJob {

    private final GitHubRepoTask gitHubRepoTask;

    public RefreshLanguagesJob(GitHubRepoTask gitHubRepoTask) {
        this.gitHubRepoTask = gitHubRepoTask;
    }

    @Scheduled(fixedDelayString = "${execution.intervalHours}", timeUnit = TimeUnit.HOURS)
    public void refreshLanguages() {
        gitHubRepoTask.processGitHubReposLanguages();
    }
}
