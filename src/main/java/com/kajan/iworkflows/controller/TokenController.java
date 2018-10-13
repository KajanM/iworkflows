package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.service.TokenControllerService;
import com.kajan.iworkflows.util.Constants.*;
import com.kajan.iworkflows.view.TokenClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static com.kajan.iworkflows.util.Constants.*;

@Controller
@RequestMapping("/token")
public class TokenController {

    private final String authorizationRequestUriTemplate = "authorize/{provider}";
    private final String authorizationRevokeUriTemplate = "/token/revoke/{provider}";
    private Logger logger = LoggerFactory.getLogger(TokenController.class);
    @Autowired
    private TokenControllerService tokenControllerService;
    @Value("${moodle.name}")
    private String MOODLE_NAME;

    @Value("${moodle.redirect-uri}")
    private String MOODLE_REDIRECT_URI;

    @Value("${msg.connect.success}")
    private String CONNECT_SUCCESS_TEMPLATE;

    @Value("${msg.connect.fail}")
    private String CONNECT_FAIL_TEMPLATE;

    @Value("${msg.disconnect.success}")
    private String DISCONNECT_SUCCESS_TEMPLATE;

    @Value("${msg.disconnect.fail}")
    private String DISCONNECT_FAIL_TEMPLATE;

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
        String authorizationRedirectUri;
        String authorizationRevokeUri;

        for (ClientRegistration registration : clientRegistrations) {
            authorizationRedirectUri = authorizationRequestUriTemplate.replace(PLACEHOLDER_PROVIDER, registration.getClientName());
            authorizationRevokeUri = authorizationRevokeUriTemplate.replace(PLACEHOLDER_PROVIDER, registration.getClientName());
            client = new TokenClient();
            client.setName(registration.getClientName());
            client.setRedirectUri(authorizationRedirectUri);
            client.setRevokeUri(authorizationRevokeUri);
            client.setAuthorized(!tokenControllerService.isAlreadyAuthorized(principal, TokenProvider.valueOf(registration.getClientName().toUpperCase())));
            if (tokenClients.contains(client)) {
                tokenClients.remove(client);
            }
            tokenClients.add(client);
        }

        client = new TokenClient();
        client.setName(MOODLE_NAME);
        client.setRedirectUri(MOODLE_REDIRECT_URI);
        client.setRevokeUri(authorizationRevokeUriTemplate.replace(PLACEHOLDER_PROVIDER, "moodle"));
        client.setAuthorized(!tokenControllerService.isAlreadyAuthorized(principal, TokenProvider.valueOf("MOODLE")));

        if (tokenClients.contains(client)) {
            tokenClients.remove(client);
        }
        tokenClients.add(client);

        model.addAttribute(CLIENTS_KEY, tokenClients);

        return "authorize";
    }

    @RequestMapping("/authorize/{registrationId}")
    public ModelAndView redirectToNextCloudForAuthorization(@PathVariable String registrationId) {

        logger.debug("hit /authorize/oauth2/" + registrationId + " end-point");

        TokenProvider tokenProvider = TokenProvider.valueOf(registrationId.toUpperCase());

        URI requestURI = tokenControllerService.getAuthorizationCodeRequestUri(tokenProvider);
        return new ModelAndView("redirect:" + requestURI.toASCIIString());
    }

    @RequestMapping("/code/{registrationId}")
    public String getNextcloudAccessToken(@PathVariable String registrationId, HttpServletRequest httpServletRequest, Principal principal, Model model, RedirectAttributes redirectAttributes) {

        TokenProvider tokenProvider = TokenProvider.valueOf(registrationId.toUpperCase());
        tokenControllerService.exchangeAuthorizationCodeForAccessToken(tokenProvider, httpServletRequest, principal);

        redirectAttributes.addAttribute(DO_NOTIFY_KEY, true);
        redirectAttributes.addAttribute(MESSAGE_KEY, CONNECT_SUCCESS_TEMPLATE.replace(PLACEHOLDER_PROVIDER, registrationId));
        redirectAttributes.addAttribute(STYLE_KEY, STYLE_SUCCESS);
        return "redirect:/token/authorize";
    }

    @PostMapping("/revoke/{registrationId}")
    public String revokeAuthorizationToken(@PathVariable String registrationId, Principal principal, RedirectAttributes redirectAttributes) {
        Boolean isSuccess = tokenControllerService.revokeToken(principal, TokenProvider.valueOf(registrationId.toUpperCase()));
        redirectAttributes.addAttribute(DO_NOTIFY_KEY, true);
        redirectAttributes.addAttribute(MESSAGE_KEY, DISCONNECT_SUCCESS_TEMPLATE.replace(PLACEHOLDER_PROVIDER, registrationId));
        redirectAttributes.addAttribute(STYLE_KEY, STYLE_SUCCESS);

        if (isSuccess) {
            return "redirect:/token/authorize";
        }
        redirectAttributes.addAttribute(MESSAGE_KEY, DISCONNECT_FAIL_TEMPLATE.replace(PLACEHOLDER_PROVIDER, registrationId));
        redirectAttributes.addAttribute(STYLE_KEY, STYLE_ERROR);

        return "redirect:/token/authorize";
    }
}
