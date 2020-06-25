package com.fsl.web.controller.home;

import com.fsl.config.EbayOAuthConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {

    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String queryParamSeparator = "&";
    private EbayOAuthConfig authConfig;

    public HomeController(EbayOAuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @GetMapping("/")
    public ModelAndView greeting(Model model) {
        model.addAttribute("today", sdf.format(new Date()));
        model.addAttribute("authUrl", getAuthorizationUrl());

        return new ModelAndView("index",model.asMap());

    }

    private String getAuthorizationUrl() {
        return  new StringBuilder().append(authConfig.getAuthUrl()).append("?")
                .append(queryParam("client_id",authConfig.getClientId()))
                .append(queryParamSeparator)
                .append(queryParam("response_type","code"))
                .append(queryParamSeparator)
                .append(queryParam("redirect_uri",authConfig.getRedirectUri()))
                .append(queryParamSeparator)
                .append(queryParam("scope",authConfig.getApplicationScopes()))
                .append(queryParamSeparator)
                .append(queryParam("prompt","login")).toString();
    }

    private String queryParam(String attName, String attValue) {
        return new StringBuilder().append(attName).append("=").append(attValue).toString();
    }
}
