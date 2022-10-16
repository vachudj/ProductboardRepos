package com.productboard.productboardrepos.github;

import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class GitHubTestUrlBuilder {
    private static final String URL_TEMPLATE = "http://%s:%s%s";
    private static final String URL_TEMPLATE_WITH_QUERY = "http://%s:%s%s?%s";
    private String host;
    private Integer port;
    private String path;
    private Map<String, String> queryParams;

    private GitHubTestUrlBuilder() {
    }

    public static GitHubTestUrlBuilder newBuilder() {
        return new GitHubTestUrlBuilder();
    }

    public GitHubTestUrlBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    public GitHubTestUrlBuilder withPort(Integer port) {
        this.port = port;
        return this;
    }

    public GitHubTestUrlBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public GitHubTestUrlBuilder withQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public String build() {
        Objects.requireNonNull(this.host, "host can't be null.");
        Objects.requireNonNull(this.port, "port can't be null.");
        Objects.requireNonNull(this.path, "path can't be null.");
        return CollectionUtils.isEmpty(queryParams) ? getUrl() : getUrlWithQuery();
    }

    private String getUrlWithQuery() {
        String query = queryParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        return URL_TEMPLATE_WITH_QUERY.formatted(host, port, path, query);
    }

    private String getUrl() {
        return URL_TEMPLATE.formatted(host, port, path);
    }
}
