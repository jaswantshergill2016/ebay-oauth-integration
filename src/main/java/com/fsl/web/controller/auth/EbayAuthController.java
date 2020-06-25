package com.fsl.web.controller.auth;

import com.fsl.domain.*;
import com.fsl.service.AuthService;
import com.fsl.web.controller.forms.RefreshTokenForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
// Web Controller handles the auth code exchanges, we should save the access and refresh token in some database and setup timestamp in the database to determine expiry time with respect to the current time
// Also showcase how to get new access token using refresh, in real world it's done in the background
public class EbayAuthController {

    private AuthService authService;

    public EbayAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth-exchange")
    public ModelAndView authExchange(@RequestParam(name="code", required=false, defaultValue="") String code, Model model) {
        log.debug("start authExchange for authorization code = {} ", code);
        // add validation if code is empty or null
        TokenGenerationResponse tokenGenerationResponse =
                authService.exchangeAuthCode(
                        AuthCodeExchangeRequest.builder().authCode(code).grantType("authorization_code").build());
        model.addAttribute("authExchange",OAuthTokens.builder().accessToken(tokenGenerationResponse.getAccessToken()).refreshToken(tokenGenerationResponse.getRefreshToken()).build());
        log.debug("end authExchange");
        return new ModelAndView("authExchange",model.asMap());
    }

    @RequestMapping(value = "/refresh-access-token", method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView refreshAccessToken(RefreshTokenForm refreshTokenForm) {
        log.debug("start refreshAccessToken for refresh token {} ", refreshTokenForm.getRefreshToken());
        // add validation if refresh token is null
        TokenGenerationResponse refreshAccessTokenResponse = authService.refreshAccessToken(RefreshAccessTokenRequest.builder().refreshToken(refreshTokenForm.getRefreshToken()).grantType("refresh_token").build());
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("authExchange",OAuthTokens.builder().accessToken(refreshAccessTokenResponse.getAccessToken()).refreshToken(refreshAccessTokenResponse.getRefreshToken()).build());
        log.debug("end refreshAccessToken");
        return new ModelAndView("authExchange",modelMap);
    }


    @GetMapping("/auth-decline")
    public ModelAndView authDecline(Model model) {
        log.debug("start authDecline");

        log.debug("end authDecline");
        return new ModelAndView("authDecline",model.asMap());
    }
}
