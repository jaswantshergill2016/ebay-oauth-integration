package com.fsl.service;


import com.fsl.config.EbayOAuthConfig;
import com.fsl.domain.AuthCodeExchangeRequest;
import com.fsl.domain.RefreshAccessTokenRequest;
import com.fsl.domain.TokenGenerationResponse;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EbayAuthService implements AuthService {

    private EbayOAuthConfig authConfig;

    public EbayAuthService(EbayOAuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @Override
    public TokenGenerationResponse exchangeAuthCode(AuthCodeExchangeRequest authCodeExchangeRequest) {
        log.debug("start exchangeAuthCode {} ",authCodeExchangeRequest);
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");

        HttpResponse<JsonNode> httpResponse = Unirest.post(authConfig.getAuthCodeExchangeUrl())
                .headers(headers)
                .basicAuth(authConfig.getClientId(), authConfig.getClientSecret())
                .field("grant_type", authCodeExchangeRequest.getGrantType())
                .field("code", authCodeExchangeRequest.getAuthCode())
                .field("redirect_uri", authConfig.getRedirectUri())
                .asJson()
                .ifFailure(response -> {
                    System.out.println("Error Status" + response.getStatus());
                    System.out.println("Error Body" + response.getBody().toString());
                    throw new RuntimeException("Cannot exchange auth code");
                });

        JSONObject jsonResponse = httpResponse.getBody().getObject();
        TokenGenerationResponse tokenGenerationResponse = TokenGenerationResponse.builder()
                .accessToken(jsonResponse.getString("access_token"))
                .refreshToken(jsonResponse.getString("refresh_token"))
                .accessCodeExpiryInSec(jsonResponse.getInt("expires_in"))
                .refreshTokenExpiryInSec(jsonResponse.getInt("refresh_token_expires_in"))
                .build();
        // we should save the access and refresh token in some database )and setup timestamp in the database to determine expiry time with respect to the current time
        // as per the observed behavior ebay access token is valid for 2 hrs and refresh tokens are valid for around 500+ days
        log.debug("end exchangeAuthCode {} ",tokenGenerationResponse);
        return tokenGenerationResponse;
    }

    @Override
    public TokenGenerationResponse refreshAccessToken(RefreshAccessTokenRequest refreshAccessTokenRequest) {
        log.debug("start refreshAccessToken {} ",refreshAccessTokenRequest);
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        HttpResponse<JsonNode> httpResponse = Unirest.post(authConfig.getRefreshAccessTokenUrl())
                .headers(headers)
                .basicAuth(authConfig.getClientId(), authConfig.getClientSecret())
                .field("grant_type", refreshAccessTokenRequest.getGrantType())
                .field("refresh_token", refreshAccessTokenRequest.getRefreshToken())
                .field("scope", authConfig.getApplicationScopes())
                .asJson()
                .ifFailure(response -> {
                    log.error("Error Status {} , Response Body ", response.getStatus(), response.getBody().toString());
                    throw new RuntimeException("Cannot get access token");
                });
        JSONObject jsonResponse = httpResponse.getBody().getObject();
        TokenGenerationResponse tokenGenerationResponse = TokenGenerationResponse.builder()
                .accessToken(jsonResponse.getString("access_token"))
                .refreshToken(refreshAccessTokenRequest.getRefreshToken())
                .accessCodeExpiryInSec(jsonResponse.getInt("expires_in"))
                .build();
        log.debug("end refreshAccessToken {} ",tokenGenerationResponse);
        return tokenGenerationResponse;
    }
}
