package com.kajan.iworkflows.service;

import com.kajan.iworkflows.model.moodle.Events;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.kajan.iworkflows.util.Constants.TokenProvider.MOODLE;

@Service
@Slf4j
public class MoodleCalendarService {
    private final OauthTokenService oauthTokenService;
    private final RestTemplate restTemplate;

    @Autowired
    public MoodleCalendarService(OauthTokenService oauthTokenService, RestTemplate restTemplate) {
        this.oauthTokenService = oauthTokenService;
        this.restTemplate = restTemplate;
    }

    public int getNumberOfEvents(int day, int month, int year, String principal) {

        String url = this.buildUrl(principal, day, month, year);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map req_payload = new HashMap();

        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        Events events = restTemplate.postForEntity(url, request, Events.class).getBody();

        if (events == null) {
            return 0;
        }
        return events.getEvents().size();
    }

    private String buildUrl(String principal, int day, int month, int year) {
        String template = "http://iworkflows.projects.mrt.ac.lk/moodle/webservice/rest/server.php?wstoken={wstoken}&wsfunction=core_calendar_get_calendar_day_view&moodlewsrestformat=json&year={year}&month={month}&day={day}";
        String wstoken = oauthTokenService.getToken(principal, MOODLE).getAccessToken().getValue();
        String uri = template.replace("{wstoken}", wstoken)
                .replace("{year}", Integer.toString(year))
                .replace("{month}", Integer.toString(month))
                .replace("{day}", Integer.toString(day));
        return uri;
    }
}
