package com.kajan.iworkflows.controller.moodle;

import com.kajan.iworkflows.service.MoodleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Controller
@RequestMapping("/moodle")
public class MoodleAssignmentController {

    private final Logger logger = LoggerFactory.getLogger(MoodleAssignmentController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MoodleService moodleService;

    @Value("${moodle.wsfunction.get-courses-by-field}")
    private String FUNCTION_GET_COURSES_BY_FIELD;

    @Value("${moodle.wsfunction.get-assignments}")
    private String FUNCTION_GET_ASSIGNMENTS;


    @GetMapping("/courses")
    @ResponseBody
    public String getCourseDetails(Principal principal) {

        String url = moodleService.buildUrl(principal, FUNCTION_GET_COURSES_BY_FIELD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map req_payload = new HashMap();

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity.toString();

    }

    @GetMapping("/assignments")
    @ResponseBody
    public String getAssignementDetails(Principal principal) {

        String url = moodleService.buildUrl(principal, FUNCTION_GET_ASSIGNMENTS);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map req_payload = new HashMap();

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return responseEntity.toString();
    }
}
