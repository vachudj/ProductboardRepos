package com.productboard.productboardrepos.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.productboard.productboardrepos.GitHubRepoLanguageUtil;
import com.productboard.productboardrepos.JsonMapper;
import com.productboard.productboardrepos.domain.GitHubRepoInfo;
import com.productboard.productboardrepos.github.GithubClient;
import com.productboard.productboardrepos.repository.LanguagesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.function.Predicate;

public class GitHubRepoTask {

    private final static Logger LOG = LoggerFactory.getLogger(GitHubRepoTask.class);

    private final GithubClient client;

    private final int initLimit;

    private final String gitHubReposUrlTemplate;

    private final LanguagesRepository languagesRepository;

    private final Predicate<ArrayNode> isAnyGitHubResponseAvailable = gitHubReposPage ->
            gitHubReposPage != null && !gitHubReposPage.isNull() && !gitHubReposPage.isEmpty();

    public GitHubRepoTask(GithubClient client,
                          LanguagesRepository languagesRepository,
                          String gitHubReposUrlTemplate,
                          int initLimit) {
        this.client = client;
        this.gitHubReposUrlTemplate = gitHubReposUrlTemplate;
        this.languagesRepository = languagesRepository;
        this.initLimit = initLimit;
    }

    public void processGitHubReposLanguages() {
        int page = 1;
        boolean isLoadedAllGitHubRepos = false;

        while (!isLoadedAllGitHubRepos) {
            ArrayNode gitHubReposPageResponse = findGitHubReposForPage(page);
            if (isAnyGitHubResponseAvailable.test(gitHubReposPageResponse)) {
                Iterator<JsonNode> elements = gitHubReposPageResponse.elements();
                while (elements.hasNext()) {
                    JsonNode element = elements.next();
                    processGitHubRepoLanguages(
                            element.get("name").asText(),
                            element.get("languages_url").asText()
                    );
                }
                page++;
            } else {
                isLoadedAllGitHubRepos = true;
            }
        }
    }

    private ArrayNode findGitHubReposForPage(int page) {
        HttpResponse<String> response = client.fromGitHub(gitHubReposUrlTemplate.formatted(page, initLimit));
        return (ArrayNode) JsonMapper.convertToJsonNode(response.body());
    }

    private void processGitHubRepoLanguages(String repoName, String url) {
        String languageJson = client.fromGitHub(url).body();
        saveRepoLanguages(repoName, languageJson);
    }

    private void saveRepoLanguages(String repoName, String languages) {
        LOG.info("Saving languages [{}] for gitHubRepo: [{}]", languages, repoName);
        GitHubRepoInfo gitHubRepoInfo;
        gitHubRepoInfo = GitHubRepoLanguageUtil.convertToGitHugRepoInfo(repoName, languages);
        languagesRepository.save(repoName, gitHubRepoInfo);
    }
}
