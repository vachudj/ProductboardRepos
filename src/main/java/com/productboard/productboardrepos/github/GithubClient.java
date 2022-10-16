package com.productboard.productboardrepos.github;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GithubClient {

    public static final Logger LOG = LoggerFactory.getLogger(GithubClient.class);

    private final HttpClient client;

    private final String apiToken;

    private final Duration requestTimeout;

    private final RetryPolicy<HttpResponse<String>> retryPolicy;

    public GithubClient(HttpClient client,
                        ClientConfiguration clientConfiguration
    ) {
        this.client = client;
        this.apiToken = clientConfiguration.apiToken();
        this.requestTimeout = clientConfiguration.requestTimeout();
        this.retryPolicy = clientConfiguration.configureRetry();
    }

    public HttpResponse<String> fromGitHub(String url) {
        CheckedSupplier<HttpResponse<String>> supplier =
                () -> invoke(URI.create(url));

        HttpResponse<String> response =
                Failsafe.with(retryPolicy).get(supplier);

        if (failedHttpResponse(response)) {
            String message = "Http request failed for url: [%s]. StatusCode: [%s], reason: [%s]"
                    .formatted(url, response.statusCode(), response.body());
            throw new RuntimeException(message);
        }

        return response;
    }

    private HttpResponse<String> invoke(URI uri) throws IOException {
        LOG.info("Trying to download data from URL: [{}]", uri);
        try {
            HttpRequest request = createRequest(uri);
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new RuntimeException("Http request failed for url: [%s]".formatted(uri), e);
        }
    }

    private HttpRequest createRequest(URI endpoint) {
        return HttpRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", "token " + apiToken)
                .timeout(requestTimeout)
                .uri(endpoint)
                .GET()
                .build();
    }

    private static boolean failedHttpResponse(HttpResponse<?> response) {
        int statusCode = response.statusCode();
        return !(statusCode >= 200 && statusCode <= 299);
    }
}
