package com.productboard.productboardrepos.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.productboard.productboardrepos.domain.GitHubRepoInfo;

import java.util.Collection;

public class CaffeineLanguagesRepository implements LanguagesRepository {

    private final Cache<String, GitHubRepoInfo> cache;

    public CaffeineLanguagesRepository(Cache<String, GitHubRepoInfo> cache) {
        this.cache = cache;
    }

    @Override
    public void save(String key, GitHubRepoInfo gitHubRepoInfo) {
        cache.put(key, gitHubRepoInfo);
    }

    @Override
    public GitHubRepoInfo getIfPresent(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public Collection<GitHubRepoInfo> getAll() {
        return cache.asMap().values();
    }
}
