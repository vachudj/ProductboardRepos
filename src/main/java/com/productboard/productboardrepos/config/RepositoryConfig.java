package com.productboard.productboardrepos.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.productboard.productboardrepos.domain.GitHubRepoInfo;
import com.productboard.productboardrepos.repository.CaffeineLanguagesRepository;
import com.productboard.productboardrepos.repository.LanguagesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RepositoryConfig {

    @Bean
    public Cache<String, GitHubRepoInfo> cache(@Value("${cache.expiration.days:10}") int expirationDays,
                                        @Value("${cache.size:200}") int cacheSize) {
        return Caffeine.newBuilder()
                .expireAfterWrite(expirationDays, TimeUnit.DAYS)
                .maximumSize(cacheSize)
                .build();
    }

    @Bean
    public LanguagesRepository languagesRepository(Cache<String, GitHubRepoInfo> cache) {
        return new CaffeineLanguagesRepository(cache);
    }
}
