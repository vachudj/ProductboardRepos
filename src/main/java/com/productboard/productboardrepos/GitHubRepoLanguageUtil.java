package com.productboard.productboardrepos;

import com.fasterxml.jackson.databind.JsonNode;
import com.productboard.productboardrepos.domain.GitHubRepoInfo;
import com.productboard.productboardrepos.domain.LanguageInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

public final class GitHubRepoLanguageUtil {

    private GitHubRepoLanguageUtil() {
    }

    public static GitHubRepoInfo convertToGitHugRepoInfo(String repoName, String languages) {
        JsonNode jsonNode = JsonMapper.convertToJsonNode(languages);
        long total = StreamSupport.stream(Spliterators.spliteratorUnknownSize(jsonNode.fields(), ORDERED), false)
                .map(e -> e.getValue().longValue())
                .mapToLong(Long::longValue)
                .sum();
        List<LanguageInfo> languagesInfo = StreamSupport.stream(Spliterators.spliteratorUnknownSize(jsonNode.fields(), ORDERED), false)
                .map(e -> new LanguageInfo(e.getKey(), calculatePercentage(total, e)))
                .toList();

        return new GitHubRepoInfo(repoName, languagesInfo);
    }

    private static BigDecimal calculatePercentage(long total, Map.Entry<String, JsonNode> e) {
        BigDecimal decimal = new BigDecimal(e.getValue().asText()).divide(new BigDecimal(total), 2, RoundingMode.HALF_DOWN);
        return decimal.multiply(new BigDecimal(100));
    }
}
