package com.kajan.iworkflows.controller.moodle;

import com.kajan.iworkflows.service.OauthTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static com.kajan.iworkflows.util.Constants.TokenProvider.MOODLE;

@Controller
@RequestMapping("/moodle")
public class MoodleAssignmentController {

    private final Logger logger = LoggerFactory.getLogger(MoodleAssignmentController.class);

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private RestTemplate restTemplate;

    private String domainName = "http://iworkflows.projects.mrt.ac.lk/moodle/webservice/rest/server.php";

    @GetMapping("/courses")
    @ResponseBody
    public String getCourseDetails(Principal principal) {

        String functionName = "core_course_get_courses_by_field";

        String wstoken = oauthTokenService.getOauth2Tokens(principal, MOODLE).getAccessToken().getValue();
        String url = domainName + "?wstoken=" + wstoken + "&wsfunction=" + functionName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map req_payload = new HashMap();

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity.toString();

    }
}
