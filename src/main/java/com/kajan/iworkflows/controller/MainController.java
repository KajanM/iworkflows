package com.kajan.iworkflows.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@Controller
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    private String clientId = "h7P5r5KAqOorccQaHezH4j7Lui5koq982AIRDz4ynRRzwl00j70ygIl5QdapjPMV";

    @GetMapping("/user")
    public String user(Principal principal, Model model) {
        logger.debug("hit /user endpoint");
        model.addAttribute("name", principal.getName());
        return "user";
    }

    @GetMapping("/authorize")
    public String authorize() {
        return "authorize";
    }

    @RequestMapping("/redirect/nextcloud")
    public ModelAndView redirectToNextCloud() {

        logger.debug("hit /login/oauth2/code/nextcloud endpoint");

        // The authorisation endpoint of the server
        URI authzEndpoint = null;
        try {
            authzEndpoint = new URI("http://localhost:8090/nextcloud/index.php/apps/oauth2/authorize");
        } catch (URISyntaxException e) {
            logger.error("Invalid authorization end point", e);
        }

        // The client identifier provisioned by the server
        ClientID clientID = new ClientID(clientId);

        // The requested scope values for the token
        Scope scope = new Scope("read", "write");

        // The client callback URI, typically pre-registered with the server
        URI callback = null;
        try {
            callback = new URI("http://localhost:8080/login/oauth2/code/nextcloud");
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
        return new ModelAndView("redirect:" + requestURI.toASCIIString());
    }

    @RequestMapping("/login/oauth2/code/nextcloud")
    public String nextcloudToken(HttpServletRequest request, Model model) {

        // Parse the authorisation response from the callback URI
        AuthorizationResponse response = null;
        try {
            response = AuthorizationResponse.parse(new URI("http://localhost:8080/login/oauth2/code/nextcloud?" + request.getQueryString()));
        } catch (ParseException e) {
            logger.error("Unable to parse authorization code response from server", e);
        } catch (URISyntaxException e) {
            logger.error("URI syntax of the authorization code response is not valid", e);
        }

        if (response != null && !response.indicatesSuccess()) {
            // TODO: The request was denied or some error may have occurred
        }

        AuthorizationSuccessResponse successResponse = (AuthorizationSuccessResponse) response;

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
            callback = new URI("http://localhost:8080/login/oauth2/code/nextcloud");
        } catch (URISyntaxException e) {
            logger.error("Invalid callback URI", e);
        }
        AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callback);

        // The credentials to authenticate the client at the token endpoint
        ClientID clientID = new ClientID(clientId);
        Secret clientSecret = new Secret("e6ZwqtkuxuKmh0Cl5mTH6s3YC8OltendKnO3e3Hej3gLdf7PntGOz1S3QEXRykaO");
        ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);

        // The token endpoint
        URI tokenEndpoint = null;
        try {
            tokenEndpoint = new URI("http://localhost:8090/nextcloud/index.php/apps/oauth2/api/v1/token");
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

        model.addAttribute("accessToken", accessToken);
        model.addAttribute("refreshToken", refreshToken);
        return "authorization-success";
    }

}
