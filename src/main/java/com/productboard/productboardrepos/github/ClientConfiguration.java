package com.productboard.productboardrepos.github;

import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public record ClientConfiguration(int retryAttempts,
                                  Duration retryDelay,
                                  Duration requestTimeout,
                                  String apiToken
) {

    public static final Logger LOG = LoggerFactory.getLogger(ClientConfiguration.class);

    public RetryPolicy<HttpResponse<String>> configureRetry() {
        return new RetryPolicy<HttpResponse<String>>()
                .handle(IOException.class, ExecutionException.class, TimeoutException.class)
                .withDelay(retryDelay)
                .handleResultIf(response -> response.statusCode() >= 400)
                .withMaxRetries(retryAttempts)
                .onFailedAttempt(e ->
                        LOG.error("An invocation failed, attempt: [{}], [{}], Retrying",
                                e.getAttemptCount(),
                                e.getLastFailure() == null ? e.getLastResult().body() : e.getLastFailure().getMessage(),
                                e.getLastFailure()))
                .onRetriesExceeded(e ->
                        LOG.error("A invocation failed, retries exceeded: [{}], [{}]",
                                e.getAttemptCount(),
                                e.getFailure() == null ? e.getResult().body() : e.getFailure().getMessage(),
                                e.getFailure()));
    }


}
