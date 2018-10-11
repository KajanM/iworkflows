package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.service.OauthControllerService;
import com.kajan.iworkflows.util.Constants.TokenProvider;
import com.kajan.iworkflows.view.TokenClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class OauthController {

    private Logger logger = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private OauthControllerService oauthControllerService;

    private final String authorizationRequestBaseUri = "authorize/oauth2";
    private final String authorizationRevokeBaseUri = "/oauth2/revoke";
    Set<TokenClient> tokenClients = new HashSet<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/authorize")
    public String getAuthorizationPage(Principal principal, Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        TokenClient client;

        for (ClientRegistration registration : clientRegistrations) {
            client = new TokenClient();
            client.setName(registration.getClientName());
            client.setRedirectUri(authorizationRequestBaseUri + "/" + registration.getRegistrationId());
            client.setRevokeUri(authorizationRevokeBaseUri + "/" + registration.getRegistrationId());
            client.setAuthorized(!oauthControllerService.alreadyAuthorized(principal, TokenProvider.valueOf(registration.getClientName().toUpperCase())));
            if (tokenClients.contains(client)) {
                tokenClients.remove(client);
            }
            tokenClients.add(client);
        }

        client = new TokenClient();
        client.setName("Moodle");
        client.setRedirectUri("/moodle/token");
        client.setRevokeUri(authorizationRevokeBaseUri + "/" + "moodle");
        client.setAuthorized(!oauthControllerService.alreadyAuthorized(principal, TokenProvider.valueOf("MOODLE")));

        if (tokenClients.contains(client)) {
            tokenClients.remove(client);
        }
        tokenClients.add(client);

        model.addAttribute("clients", tokenClients);

        return "authorize";
    }

    @RequestMapping("/authorize/oauth2/{registrationId}")
    public ModelAndView redirectToNextCloudForAuthorization(@PathVariable String registrationId) {

        logger.debug("hit /authorize/oauth2/" + registrationId + " end-point");

        TokenProvider tokenProvider = TokenProvider.valueOf(registrationId.toUpperCase());

        URI requestURI = oauthControllerService.getAuthorizationCodeRequestUri(tokenProvider);
        return new ModelAndView("redirect:" + requestURI.toASCIIString());
    }

    @RequestMapping("/login/oauth2/code/{registrationId}")
    public String getNextcloudAccessToken(@PathVariable String registrationId, HttpServletRequest httpServletRequest, Principal principal, Model model, RedirectAttributes redirectAttributes) {

        logger.debug("hit /login/oauth2/code/" + registrationId + " end-point");

        TokenProvider tokenProvider = TokenProvider.valueOf(registrationId.toUpperCase());
        oauthControllerService.exchangeAuthorizationCodeForAccessToken(tokenProvider, httpServletRequest, principal);

        redirectAttributes.addAttribute("notify", true);
        redirectAttributes.addAttribute("message", "Successfully connected with " + registrationId);
        redirectAttributes.addAttribute("style", "success");
        return "redirect:/authorize";
    }

    @GetMapping("/oauth2/revoke/{registrationId}")
    public String revokeAuthorizationToken(@PathVariable String registrationId, Principal principal, RedirectAttributes redirectAttributes) {
        Boolean isSuccess = oauthControllerService.revokeOauth2Token(principal, TokenProvider.valueOf(registrationId.toUpperCase()));
        redirectAttributes.addAttribute("notify", isSuccess);
        redirectAttributes.addAttribute("message", "Successfully disconnected from " + registrationId);
        redirectAttributes.addAttribute("style", "success");

        if (isSuccess) {
            return "redirect:/authorize";
        }
        redirectAttributes.addAttribute("message", "Unable to disconnect " + registrationId + " ,please try again");
        redirectAttributes.addAttribute("style", "error");

        return "redirect:/authorize";
    }
}
