package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.service.OauthControllerService;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.util.Constants.OauthRegistrationId;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@Service
public class OauthControllerServiceImpl implements OauthControllerService {

    private final Logger logger = LoggerFactory.getLogger(OauthControllerServiceImpl.class);

    private final String baseUri = "http://localhost:8080";

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public URI getAuthorizationCodeRequestUri(OauthRegistrationId oauthRegistrationId) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(oauthRegistrationId.getRegistrationId());
        // The authorisation endpoint of the server
        URI authzEndpoint = null;
        try {
            authzEndpoint = new URI(clientRegistration.getProviderDetails().getAuthorizationUri());
        } catch (URISyntaxException e) {
            logger.error("Invalid authorization end point", e);
        }

        // The client identifier provisioned by the server
        ClientID clientID = new ClientID(clientRegistration.getClientId());

        // The requested scope values for the token
        Scope scope = new Scope(clientRegistration.getScopes().toArray(new String[clientRegistration.getScopes().size()]));


        // The client callback URI, typically pre-registered with the server
        URI callback = null;
        try {
            callback = new URI(buildRedirectUri(oauthRegistrationId));
        } catch (URISyntaxException e) {
            logger.error("invalid redirect URI", e);
        }

        // Generate random state string for pairing the response to the request
        State state = new State();

        // Build the request
        AuthorizationRequest request = new AuthorizationRequest.Builder(
                new ResponseType(ResponseType.Value.CODE), clientID)
                .scope(scope)
                .state(state)
                .redirectionURI(callback)
                .endpointURI(authzEndpoint)
                .build();

        // Use this URI to send the end-user's browser to the server
        URI requestURI = request.toURI();
        return requestURI;
    }

    @Override
    public void exchangeAuthorizationCodeForAccessToken(OauthRegistrationId registrationId, HttpServletRequest httpServletRequest, Principal principal) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId.getRegistrationId());

        // Parse the authorisation response from the callback URI
        AuthorizationResponse authorizationResponse = null;
        try {
            authorizationResponse = AuthorizationResponse.parse(new URI(buildRedirectUri(registrationId) + "?" + httpServletRequest.getQueryString()));
        } catch (ParseException e) {
            logger.error("Unable to parse authorization code response from server", e);
        } catch (URISyntaxException e) {
            logger.error("URI syntax of the authorization code response is not valid", e);
        }

        if (authorizationResponse != null && !authorizationResponse.indicatesSuccess()) {
            // TODO: The request was denied or some error may have occurred
        }

        AuthorizationSuccessResponse successResponse = (AuthorizationSuccessResponse) authorizationResponse;

        // The returned state parameter must match the one send with the request
        //if (! state.equals(successResponse.getState()) {
        // Unexpected or tampered response, stop!!!
        //}

        // Retrieve the authorisation code, to be used later to exchange the code for
        // an access token at the token endpoint of the server
        AuthorizationCode code = successResponse.getAuthorizationCode();

        // Construct the code grant from the code obtained from the authz endpoint
        // and the original callback URI used at the authz endpoint
        //AuthorizationCode code = new AuthorizationCode("xyz...");
        URI callback = null;
        try {
            callback = new URI(buildRedirectUri(registrationId));
        } catch (URISyntaxException e) {
            logger.error("Invalid callback URI", e);
        }
        AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callback);

        // The credentials to authenticate the client at the token endpoint
        ClientID clientID = new ClientID(clientRegistration.getClientId());
        Secret clientSecret = new Secret(clientRegistration.getClientSecret());
        ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);

        // The token endpoint
        URI tokenEndpoint = null;
        try {
            tokenEndpoint = new URI(clientRegistration.getProviderDetails().getTokenUri());
        } catch (URISyntaxException e) {
            logger.error("Invalid token end point URI", e);
        }

        // Make the token request
        TokenRequest tokenRequest = new TokenRequest(tokenEndpoint, clientAuth, codeGrant);

        TokenResponse tokenResponse = null;
        try {
            tokenResponse = TokenResponse.parse(tokenRequest.toHTTPRequest().send());
        } catch (ParseException e) {
            logger.error("Unable to parse response from token endpoint", e);
        } catch (IOException e) {
            logger.error("Unable to reach token end point", e);
        }

        if (tokenResponse != null && !tokenResponse.indicatesSuccess()) {
            // We got an error response...
            TokenErrorResponse errorResponse = tokenResponse.toErrorResponse();
        }

        AccessTokenResponse successTokenResponse = tokenResponse.toSuccessResponse();

        // Get the access token, the server may also return a refresh token
        AccessToken accessToken = successTokenResponse.getTokens().getAccessToken();
        RefreshToken refreshToken = successTokenResponse.getTokens().getRefreshToken();


        oauthTokenService.setAuthorizationCode(principal, code);
        oauthTokenService.setAccessToken(principal, accessToken);
        oauthTokenService.setRefreshToken(principal, refreshToken);
    }

    @Override
    public AccessToken getAccessToken(Principal principal) {
        return oauthTokenService.getAccessToken(principal);
    }

    @Override
    public RefreshToken getRefreshToken(Principal principal) {
        return oauthTokenService.getRefreshToken(principal);
    }

    private String buildRedirectUri(OauthRegistrationId oauthRegistrationId) {
        String redirectUri = clientRegistrationRepository.findByRegistrationId(oauthRegistrationId.getRegistrationId()).getRedirectUriTemplate()
                .replace("{baseUrl}", baseUri)
                .replace("{registrationId}", oauthRegistrationId.getRegistrationId());
        logger.debug("Redirect URI: " + redirectUri);
        return redirectUri;
    }
}
