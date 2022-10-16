package com.productboard.productboardrepos.github;

import com.productboard.productboardrepos.common.MockServerContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import static org.mockserver.model.HttpResponse.response;

@Testcontainers
class GithubClientTest {

    private static final String PATH = "/users/productboard/repos";

    private static MockServerClient WEB_GITHUB_CLIENT;

    @Container
    private static final MockServerContainer MOCK_SERVER_CONTAINER = new MockServerContainer();

    @BeforeAll
    static void setup() {
        WEB_GITHUB_CLIENT = new MockServerClient(MOCK_SERVER_CONTAINER.getHost(), MOCK_SERVER_CONTAINER.getFirstMappedPort());
    }

    @BeforeEach
    public void setupForEach() {
        WEB_GITHUB_CLIENT.reset();
    }

    @Test
    public void shouldReturnSuccessResponse() {
        WEB_GITHUB_CLIENT
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withQueryStringParameter("page", "1")
                        .withQueryStringParameter("per_page", "2")
                        .withHeader(Header.header("Accept", "application/json"))
                        .withHeader(Header.header("Authorization", "token " + "apiToken"))
                        .withPath(PATH)
                )
                .respond(response()
                        .withStatusCode(200)
                );

        HttpResponse<String> response = new GithubClient(HttpClient.newHttpClient(), initConfiguration(1))
                .fromGitHub(
                        getNoQueryUrlBuilder()
                                .withQueryParams(Map.of("page", "1", "per_page", "2"))
                                .build()
                );

        Assertions.assertThat(response.statusCode()).isEqualTo(200);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void shouldRetryServerErrorResponse(int retryAttemps) {
        WEB_GITHUB_CLIENT
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath(PATH))
                .respond(response()
                        .withStatusCode(500)
                );

        String url = getNoQueryUrlBuilder().build();
        try {
            new GithubClient(HttpClient.newHttpClient(), initConfiguration(retryAttemps)).fromGitHub(url);
            Assertions.fail("The exception should be thrown!");
        } catch (RuntimeException e) {
            WEB_GITHUB_CLIENT.verify(HttpRequest.request(PATH), VerificationTimes.exactly(retryAttemps + 1));
            Assertions.assertThat(e.getMessage()).isEqualTo("Http request failed for url: [%s]. StatusCode: [500], reason: []".formatted(url));
        }
    }

    private static GitHubTestUrlBuilder getNoQueryUrlBuilder() {
        return GitHubTestUrlBuilder.newBuilder()
                .withHost(MOCK_SERVER_CONTAINER.getHost())
                .withPort(MOCK_SERVER_CONTAINER.getFirstMappedPort())
                .withPath(PATH);
    }

    private static ClientConfiguration initConfiguration(Integer retryAttempts) {
        return new ClientConfiguration(
                retryAttempts,
                Duration.ofMillis(100),
                Duration.ofMillis(200),
                "apiToken"
        );
    }
}