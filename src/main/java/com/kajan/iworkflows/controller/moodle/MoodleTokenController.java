package com.kajan.iworkflows.controller.moodle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kajan.iworkflows.dto.Oauth2TokenDTO;
import com.kajan.iworkflows.service.OauthTokenService;
import com.nimbusds.oauth2.sdk.token.TypelessAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

import static com.kajan.iworkflows.util.Constants.OauthProvider.MOODLE;

@Controller
public class MoodleTokenController {

    private final Logger logger = LoggerFactory.getLogger(MoodleTokenController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OauthTokenService oauthTokenService;

    @GetMapping("/moodle/token")
    public String getLoginPageForMoodle() {
        return "moodle-login";
    }

    @PostMapping(value = "/moodle/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getMoodleWebServiceToken(@RequestParam("username") String username, @RequestParam("password") String password, Principal principal, Model model, RedirectAttributes redirectAttributes) {
        logger.debug("Username: " + username + " Password: " + password);
        ResponseEntity<String> response = restTemplate.getForEntity("http://iworkflows.projects.mrt.ac.lk/moodle/login/token.php?username=" + username + "&password=" + password + "&service=moodle_mobile_app", String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            logger.error("Unable to parse the response from moodle", e);
            model.addAttribute("error", root.get("Unable to parse the response from moodle"));
        }
        if (root.hasNonNull("error")) {
            model.addAttribute("error", root.get("error"));
        }

        if (root.hasNonNull("token")) {
            Oauth2TokenDTO oauth2TokenDTO = new Oauth2TokenDTO();
            oauth2TokenDTO.setAccessToken(new TypelessAccessToken(root.get("token").textValue()));
            oauth2TokenDTO.setOauthProvider(MOODLE);
            oauthTokenService.setOauth2Tokens(principal, oauth2TokenDTO);
            redirectAttributes.addAttribute("notify", true);
            redirectAttributes.addAttribute("message", "Successfully connected with moodle");
            redirectAttributes.addAttribute("style", "success");
            return "redirect:/authorize";
        }

        return "moodle-login";
    }
}
