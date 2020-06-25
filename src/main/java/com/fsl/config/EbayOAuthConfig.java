package com.fsl.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@ConfigurationProperties(prefix = "ebay")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EbayOAuthConfig {

    private String authUrl;
    private String authCodeExchangeUrl;
    private String refreshAccessTokenUrl;
    private String clientId;
    private String clientSecret;
    private String additionalParams;
    private String redirectUri;

    @Builder.Default
    private Set<String> scopes = new HashSet<>();

    private String scopeBaseUrl;

    public String getApplicationScopes() {
        return Stream.concat(Stream.of(scopeBaseUrl), scopes.stream().map(scope -> new StringBuilder().append(scopeBaseUrl).append("/").append(scope).toString())).collect(Collectors.joining(" "));
    }
}
