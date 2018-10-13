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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.*;
import static com.kajan.iworkflows.util.Constants.TokenProvider.MOODLE;

@Controller
@RequestMapping("moodle/token")
public class MoodleTokenController {

    private final Logger logger = LoggerFactory.getLogger(MoodleTokenController.class);

    @Value("${msg.connect.success}")
    private String CONNECT_SUCCESS_TEMPLATE;

    @Value("${moodle.uri.token}")
    private String TOKEN_URI_TEMPLATE;

    @Value("${moodle.wsshortname}")
    private String WS_SHORT_NAME;

    @Value("${moodle.name}")
    private String MOODLE_NAME;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OauthTokenService oauthTokenService;

    @GetMapping
    public String getLoginPageForMoodle() {
        return "moodle-login";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getMoodleWebServiceToken(@RequestParam(USERNAME_KEY) String username, @RequestParam(PASSWORD_KEY) String password, Principal principal, Model model, RedirectAttributes redirectAttributes) {
        ResponseEntity<String> response = restTemplate.getForEntity(TOKEN_URI_TEMPLATE, String.class, username, password, WS_SHORT_NAME);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            logger.error("Unable to parse the response from moodle", e);
            model.addAttribute(ERROR_KEY, root.get(ERROR_KEY));
        }
        if (root.hasNonNull(ERROR_KEY)) {
            model.addAttribute(ERROR_KEY, root.get(ERROR_KEY));
        }

        if (root.hasNonNull(TOKEN_KEY)) {
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setAccessToken(new TypelessAccessToken(root.get(TOKEN_KEY).textValue()));
            tokenDTO.setTokenProvider(MOODLE);
            oauthTokenService.setToken(principal, tokenDTO);
            redirectAttributes.addAttribute(DO_NOTIFY_KEY, true);
            redirectAttributes.addAttribute(MESSAGE_KEY, CONNECT_SUCCESS_TEMPLATE.replace(PLACEHOLDER_PROVIDER, MOODLE_NAME));
            redirectAttributes.addAttribute(STYLE_KEY, STYLE_SUCCESS);
            return "redirect:/token/authorize";
        }

        return "moodle-login";
    }
}
