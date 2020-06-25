package com.fsl.service;

import com.fsl.domain.AuthCodeExchangeRequest;
import com.fsl.domain.TokenGenerationResponse;
import com.fsl.domain.RefreshAccessTokenRequest;

public interface AuthService {

    TokenGenerationResponse exchangeAuthCode(AuthCodeExchangeRequest authCodeExchangeRequest);

    TokenGenerationResponse refreshAccessToken(RefreshAccessTokenRequest refreshAccessTokenRequest);
}
