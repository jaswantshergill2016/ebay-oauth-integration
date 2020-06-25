package com.fsl.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OAuthTokens {
    private String accessToken;
    private String refreshToken;
}
