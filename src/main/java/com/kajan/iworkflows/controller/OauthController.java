package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.service.OauthControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

@Controller
public class OauthController {

    private Logger logger = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private OauthControllerService oauthControllerService;

    @GetMapping("/authorize")
    public String getAuthorizationPage() {
        return "authorize";
    }

    @RequestMapping("/redirect/nextcloud")
    public ModelAndView redirectToNextCloudForAuthorization() {

        logger.debug("hit /redirect/nextcloud end-point");

        URI requestURI = oauthControllerService.getAuthorizationCodeRequestUri();
        return new ModelAndView("redirect:" + requestURI.toASCIIString());
    }

    @RequestMapping("/login/oauth2/code/nextcloud")
    public String getNextcloudAccessToken(HttpServletRequest httpServletRequest, Principal principal, Model model) {

        logger.debug("hit /login/oauth2/code/nextcloud end-point");

        oauthControllerService.exchangeAuthorizationCodeForAccessToken(httpServletRequest, principal);
        model.addAttribute("accessToken", oauthControllerService.getAccessToken(principal));
        model.addAttribute("refreshToken", oauthControllerService.getRefreshToken(principal));
        return "authorization-success";
    }
}
