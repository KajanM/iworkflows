package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.service.OauthControllerService;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.OauthProvider;
import com.kajan.iworkflows.view.OauthClient;
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
    Set<OauthClient> oauthClients = new HashSet<>();

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

        OauthClient client;

        for (ClientRegistration registration : clientRegistrations) {
            client = new OauthClient();
            client.setName(registration.getClientName());
            client.setRedirectUri(authorizationRequestBaseUri + "/" + registration.getRegistrationId());
            client.setRevokeUri(authorizationRevokeBaseUri + "/" + registration.getRegistrationId());
            client.setAuthorized(!oauthControllerService.alreadyAuthorized(principal, OauthProvider.valueOf(registration.getClientName().toUpperCase())));
            if (oauthClients.contains(client)) {
                oauthClients.remove(client);
            }
            oauthClients.add(client);
        }

        model.addAttribute("clients", oauthClients);

        return "authorize";
    }

    @RequestMapping("/authorize/oauth2/{registrationId}")
    public ModelAndView redirectToNextCloudForAuthorization(@PathVariable String registrationId) {

        logger.debug("hit /authorize/oauth2/" + registrationId + " end-point");

        OauthProvider oauthProvider = OauthProvider.valueOf(registrationId.toUpperCase());

        URI requestURI = oauthControllerService.getAuthorizationCodeRequestUri(oauthProvider);
        return new ModelAndView("redirect:" + requestURI.toASCIIString());
    }

    @RequestMapping("/login/oauth2/code/{registrationId}")
    public String getNextcloudAccessToken(@PathVariable String registrationId, HttpServletRequest httpServletRequest, Principal principal, Model model) {

        logger.debug("hit /login/oauth2/code/" + registrationId + " end-point");

        OauthProvider oauthProvider = Constants.OauthProvider.valueOf(registrationId.toUpperCase());
        oauthControllerService.exchangeAuthorizationCodeForAccessToken(oauthProvider, httpServletRequest, principal);
        Oauth2TokenDTO oauth2TokenDTO = oauthControllerService.getOauth2Tokens(principal, oauthProvider);
        model.addAttribute("authorizationProvider", oauth2TokenDTO.getOauthProvider().getProvider());
        model.addAttribute("authorizationCode", oauth2TokenDTO.getAuthorizationCode());
        model.addAttribute("accessToken", oauth2TokenDTO.getAccessToken());
        model.addAttribute("refreshToken", oauth2TokenDTO.getRefreshToken());
        return "authorization-success";
    }

    @GetMapping("/oauth2/revoke/{registrationId}")
    public String revokeAuthorizationToken(@PathVariable String registrationId, Principal principal, RedirectAttributes redirectAttributes) {
        Boolean success = oauthControllerService.revokeOauth2Token(principal, OauthProvider.valueOf(registrationId.toUpperCase()));
        redirectAttributes.addAttribute("revoked", success);
        redirectAttributes.addAttribute("message", "Successfully disconnected from " + registrationId);
        redirectAttributes.addAttribute("style", "success");

        if (success) {
            return "redirect:/authorize";
        }
        redirectAttributes.addAttribute("message", "Unable to disconnect " + registrationId + " ,please try again");
        redirectAttributes.addAttribute("style", "error");

        return "redirect:/authorize";
    }
}
