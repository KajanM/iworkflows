package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.service.TokenControllerService;
import com.kajan.iworkflows.util.Constants;
import com.kajan.iworkflows.util.Constants.TokenProvider;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@Service
public class TokenControllerServiceImpl implements TokenControllerService {

    private final Logger logger = LoggerFactory.getLogger(TokenControllerServiceImpl.class);

    private OauthTokenService oauthTokenService;

    private ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public URI getAuthorizationCodeRequestUri(TokenProvider tokenProvider) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(tokenProvider.getProvider());
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
        Scope scope = new Scope(clientRegistration.getScopes().toArray(new String[0]));


        // The client callback URI, typically pre-registered with the server
        URI callback = null;
        try {
            callback = new URI(buildRedirectUri(tokenProvider));
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
    public void exchangeAuthorizationCodeForAccessToken(Constants.TokenProvider registrationId, String queryParams, Principal principal) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId.getProvider());

        // Parse the authorisation response from the callback URI
        AuthorizationResponse authorizationResponse = null;
        try {
            authorizationResponse = AuthorizationResponse.parse(new URI(buildRedirectUri(registrationId) + queryParams));
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

        TokenResponse tokenResponse;
        AccessToken accessToken;
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAuthorizationCode(code);
        tokenDTO.setTokenProvider(registrationId);
        try {
            HTTPRequest request = tokenRequest.toHTTPRequest();
            //request.setContentType("application/json");
            HTTPResponse response = request.send();

            if (registrationId != TokenProvider.GITHUB) {
                tokenResponse = TokenResponse.parse(request.send());
                if (tokenResponse != null && !tokenResponse.indicatesSuccess()) {
                    // We got an error response...
                    TokenErrorResponse errorResponse = tokenResponse.toErrorResponse();
                }

                AccessTokenResponse successTokenResponse = tokenResponse.toSuccessResponse();

                // Get the access token, the server may also return a refresh token
                accessToken = successTokenResponse.getTokens().getAccessToken();
                RefreshToken refreshToken = successTokenResponse.getTokens().getRefreshToken();
                tokenDTO.setAccessToken(accessToken);
                tokenDTO.setRefreshToken(refreshToken);

            } else {
                // TODO: temporary fix to get access token from github
                String content = response.getContent();
                String accessTokenStr = content.substring(content.indexOf("=") + 1, content.indexOf("&"));
                accessToken = new BearerAccessToken(accessTokenStr);
                tokenDTO.setAccessToken(accessToken);
            }
        } catch (ParseException e) {
            logger.error("Unable to parse response from token endpoint", e);
        } catch (IOException e) {
            logger.error("Unable to reach token end point", e);
        }


        oauthTokenService.setToken(principal.getName(), tokenDTO);
    }

    @Override
    public TokenDTO getToken(Principal principal, TokenProvider tokenProvider) {
        return oauthTokenService.getToken(principal.getName(), tokenProvider);
    }

    private String buildRedirectUri(Constants.TokenProvider tokenProvider) {
        String redirectUri = clientRegistrationRepository.findByRegistrationId(tokenProvider.getProvider()).getRedirectUriTemplate();
        //.replace("{baseUrl}", baseUri)
        //.replace("{registrationId}", tokenProvider.getProvider());
        logger.debug("Redirect URI: " + redirectUri);
        return redirectUri;
    }

    @Override
    public Boolean isAlreadyAuthorized(Principal principal, TokenProvider provider) {
        return oauthTokenService.isAlreadyAuthorized(principal.getName(), provider);
    }

    @Override
    public Boolean revokeToken(Principal principal, Constants.TokenProvider provider) {
        return oauthTokenService.revokeToken(principal.getName(), provider);
    }

    @Autowired
    public void setOauthTokenService(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;
    }

    @Autowired
    public void setClientRegistrationRepository(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }
}
