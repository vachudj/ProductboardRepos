package com.productboard.productboardrepos.repository;

import com.productboard.productboardrepos.domain.GitHubRepoInfo;

import java.util.Collection;

public interface LanguagesRepository {

    void save(String key, GitHubRepoInfo gitHubRepoInfo);

    GitHubRepoInfo getIfPresent(String key);

    Collection<GitHubRepoInfo> getAll();
}
