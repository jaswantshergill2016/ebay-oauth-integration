package com.fsl.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AuthCodeExchangeRequest {
    private String authCode;
    private String grantType;
}
