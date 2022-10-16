package com.productboard.productboardrepos.controller;

import com.productboard.productboardrepos.domain.GitHubRepoInfo;
import com.productboard.productboardrepos.repository.LanguagesRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/percentage")
public class ProductboardController implements Api {

    private final LanguagesRepository languagesRepository;

    public ProductboardController(LanguagesRepository languagesRepository) {
        this.languagesRepository = languagesRepository;
    }

    @Override
    public Collection<GitHubRepoInfo> getAllGitRepoInfo() {
        return languagesRepository.getAll();
    }

    @Override
    public GitHubRepoInfo getSpecificGitRepoInfo(String repoName) {
        return languagesRepository.getIfPresent(repoName);
    }
}
