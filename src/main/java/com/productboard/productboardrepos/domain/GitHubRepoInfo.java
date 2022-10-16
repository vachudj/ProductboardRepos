package com.productboard.productboardrepos.domain;

import java.util.List;

public record GitHubRepoInfo(String repositoryName, List<LanguageInfo> languagesInfo) {
}
