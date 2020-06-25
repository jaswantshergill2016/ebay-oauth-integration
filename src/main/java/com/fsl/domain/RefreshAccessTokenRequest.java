package com.fsl.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RefreshAccessTokenRequest {
    private String refreshToken;
    private String grantType;
}
