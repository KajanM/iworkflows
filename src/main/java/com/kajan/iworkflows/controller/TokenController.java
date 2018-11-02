package com.kajan.iworkflows.controller;

import com.kajan.iworkflows.exception.NoSuchClientRegistrationException;
import com.kajan.iworkflows.service.TokenControllerService;
import com.kajan.iworkflows.util.Constants.*;
import com.kajan.iworkflows.view.TokenClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.security.Principal;
import java.util.*;

import static com.kajan.iworkflows.util.Constants.*;

@RestController
@RequestMapping("api/v1/token")
@Slf4j
public class TokenController {

    private final String authorizationRequestUriTemplate = "authorize/{provider}";
    private final String authorizationRevokeUriTemplate = "/token/revoke/{provider}";

    private final TokenControllerService tokenControllerService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    private final String MOODLE_NAME;
    private final String MOODLE_REDIRECT_URI;
    private final String CONNECT_SUCCESS_TEMPLATE;
    private final String CONNECT_FAIL_TEMPLATE;
    private final String DISCONNECT_SUCCESS_TEMPLATE;
    private final String DISCONNECT_FAIL_TEMPLATE;

    Set<TokenClient> tokenClients = new HashSet<>();

    @Autowired
    public TokenController(TokenControllerService tokenControllerService, @Value("${moodle.name}") String moodle_name,
                           @Value("${moodle.redirect-uri}") String moodle_redirect_uri,
                           @Value("${msg.connect.success}") String connect_success_template,
                           @Value("${msg.connect.fail}") String connect_fail_template,
                           @Value("${msg.disconnect.success}") String disconnect_success_template,
                           @Value("${msg.disconnect.fail}") String disconnect_fail_template, ClientRegistrationRepository clientRegistrationRepository) {
        this.tokenControllerService = tokenControllerService;
        MOODLE_NAME = moodle_name;
        MOODLE_REDIRECT_URI = moodle_redirect_uri;
        CONNECT_SUCCESS_TEMPLATE = connect_success_template;
        CONNECT_FAIL_TEMPLATE = connect_fail_template;
        DISCONNECT_SUCCESS_TEMPLATE = disconnect_success_template;
        DISCONNECT_FAIL_TEMPLATE = disconnect_fail_template;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/providers")
    public Collection<TokenClient> getTokenProviders(Principal principal) {
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
            client.setAuthorized(tokenControllerService.isAlreadyAuthorized(principal, TokenProvider.valueOf(registration.getClientName().toUpperCase())));
            if (tokenClients.contains(client)) {
                tokenClients.remove(client);
            }
            tokenClients.add(client);
        }

        client = new TokenClient();
        client.setName(MOODLE_NAME);
        client.setRedirectUri(MOODLE_REDIRECT_URI);
        client.setRevokeUri(authorizationRevokeUriTemplate.replace(PLACEHOLDER_PROVIDER, "moodle"));
        client.setAuthorized(tokenControllerService.isAlreadyAuthorized(principal, TokenProvider.valueOf("MOODLE")));

        if (tokenClients.contains(client)) {
            tokenClients.remove(client);
        }
        tokenClients.add(client);

        return tokenClients;
    }

    @RequestMapping("/authorize/{registrationId}")
    public Map<String, String> getRedirectUriToGetAuthorizationCode(@PathVariable String registrationId) {

        log.debug("hit /authorize/oauth2/" + registrationId + " end-point");

        TokenProvider tokenProvider;
        try {
            tokenProvider = TokenProvider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NoSuchClientRegistrationException(registrationId);
        }

        URI requestURI = tokenControllerService.getAuthorizationCodeRequestUri(tokenProvider);
        Map<String, String> data = new HashMap<>();
        data.put("redirect_uri", requestURI.toASCIIString());
        return data;
    }

    @PostMapping("/code")
    public ResponseEntity<?> getAccessToken(@RequestBody Map<String, Object> payload, Principal principal) {
        log.debug("Payload received: {}", payload);
        String providerId = ((String) payload.get("provider"));
        String queryParams = ((String) payload.get("query"));

        TokenProvider tokenProvider = TokenProvider.valueOf(providerId.toUpperCase());
        tokenControllerService.exchangeAuthorizationCodeForAccessToken(tokenProvider, queryParams, principal);

        return new ResponseEntity<>(HttpStatus.OK);
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
