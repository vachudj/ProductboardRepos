package com.productboard.productboardrepos.controller;

import com.productboard.productboardrepos.domain.GitHubRepoInfo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.validation.constraints.NotNull;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/")
@OpenAPIDefinition(
        info = @Info(version = "v1", description = "Service API to get productboard repository percentage cover of languages", title = "Producboard Repos API"))
public interface Api {

    @Operation(summary = "Get collection of github repository info. ")
    @ApiResponse(responseCode = "200", description = "Collection of info for all available repositories.")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping(path = "/percentage/all", produces = APPLICATION_JSON_VALUE)
    Collection<GitHubRepoInfo> getAllGitRepoInfo();

    @Operation(summary = "Get specific github repository info. ")
    @ApiResponse(responseCode = "200", description = "Info for require github repository.")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping(path = "/percentage/{repoName}", produces = APPLICATION_JSON_VALUE)
    GitHubRepoInfo getSpecificGitRepoInfo(@PathVariable @NotNull String repoName);

}
