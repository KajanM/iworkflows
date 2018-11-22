package com.kajan.iworkflows.controller.moodle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.service.OauthTokenService;
import com.nimbusds.oauth2.sdk.token.TypelessAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.*;
import static com.kajan.iworkflows.util.Constants.TokenProvider.MOODLE;

@RestController
@RequestMapping("api/v1/moodle/")
public class MoodleTokenController {

    private final Logger logger = LoggerFactory.getLogger(MoodleTokenController.class);

    @Value("${moodle.uri.token}")
    private String TOKEN_URI_TEMPLATE;

    @Value("${moodle.wsshortname}")
    private String WS_SHORT_NAME;

    @Value("${moodle.name}")
    private String MOODLE_NAME;

    private final RestTemplate restTemplate;
    private final OauthTokenService oauthTokenService;

    @Autowired
    public MoodleTokenController(RestTemplate restTemplate, OauthTokenService oauthTokenService) {
        this.restTemplate = restTemplate;
        this.oauthTokenService = oauthTokenService;
    }

    @PostMapping(value = "token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> getMoodleWebServiceToken(@RequestParam(USERNAME_KEY) String username, @RequestParam(PASSWORD_KEY) String password, Principal principal) {
        ResponseEntity<String> response = restTemplate.getForEntity(TOKEN_URI_TEMPLATE, String.class, username, password, WS_SHORT_NAME);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            logger.error("Unable to parse the response from moodle", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (root.hasNonNull(ERROR_KEY)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (root.hasNonNull(TOKEN_KEY)) {
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setAccessToken(new TypelessAccessToken(root.get(TOKEN_KEY).textValue()));
            tokenDTO.setTokenProvider(MOODLE);
            oauthTokenService.setToken(principal.getName(), tokenDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
