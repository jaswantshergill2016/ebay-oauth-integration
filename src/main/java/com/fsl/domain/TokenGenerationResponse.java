package com.fsl.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class TokenGenerationResponse {

    private String accessToken;
    private String refreshToken;
    private int accessCodeExpiryInSec;
    private int refreshTokenExpiryInSec;

}
