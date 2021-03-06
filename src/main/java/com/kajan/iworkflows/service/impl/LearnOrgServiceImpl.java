package com.kajan.iworkflows.service.impl;

import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.exception.IworkflowsPreConditionRequiredException;
import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.model.UserStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
import com.kajan.iworkflows.service.LearnOrgService;
import com.kajan.iworkflows.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import static com.kajan.iworkflows.util.Constants.*;
import static com.kajan.iworkflows.util.Constants.TokenProvider.LEARNORG;

@Service
@Slf4j
public class LearnOrgServiceImpl implements LearnOrgService {
    private final OauthTokenService oauthTokenService;
    private final RestTemplate restTemplate;
    private final String userInfoUri;
    private final String username;
    private final String password;
    private final String HEADER_VALUE_BASIC = "Basic ";
    private final LogStoreRepository logStoreRepository;
    private Timestamp timestamp;

    @Autowired
    public LearnOrgServiceImpl(@Value("${learnorg.uri.userinfo}") String userInforUri,
                               OauthTokenService oauthTokenService, RestTemplate restTemplate,
                               @Value("${iworkflows.credentials.learnorg.username}") String username,
                               @Value("${iworkflows.credentials.learnorg.password}") String password,
                               LogStoreRepository logStoreRepository) {
        this.oauthTokenService = oauthTokenService;
        this.restTemplate = restTemplate;
        this.userInfoUri = userInforUri;
        this.username = username;
        this.password = password;
        this.logStoreRepository = logStoreRepository;
    }

    @Override
    public UserStore getLearnOrgUserInfo(Principal principal) {
        try {
            TokenDTO tokenDTO = this.oauthTokenService.getToken(principal.getName(), LEARNORG);

            if (tokenDTO == null) {
                log.warn("No LearnOrg access token found for {}", principal.getName());
                timestamp = new Timestamp(System.currentTimeMillis());
                logStoreRepository.save(new LogStore(principal.getName(), timestamp, "No LearnOrg access token found for " + principal.getName()));
                throw new IworkflowsPreConditionRequiredException("No LearnOrg access token found for " + principal.getName());
            }
            String accesstoken = tokenDTO.getAccessToken().getValue();

            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add(ACCESS_TOKEN, accesstoken);
            map.add(USERNAME_KEY, principal.getName());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, getLearnOrgHeaders());
            log.debug("fetching data from learnorg user info uri {} : " + userInfoUri);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal.getName(), timestamp, "fetching data from learnorg user info uri  " + userInfoUri));
            ResponseEntity<UserStore> response = this.restTemplate.postForEntity(userInfoUri, request, UserStore.class);
            log.debug("Response from Learnorg---------" + response.getBody());
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Response from Learnorg--------- " + response.getBody()));
            return response.getBody();
        } catch (IworkflowsPreConditionRequiredException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unable to get user info from LearnOrg", e);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(principal.getName(), timestamp, "Unable to get user info from LearnOrg" + e));
            throw new IworkflowsPreConditionRequiredException("Unable to get user info from LearnOrg");
        }
    }

    public HttpHeaders getLearnOrgHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public HttpHeaders getLearnOrgHeadersAsIworkflows() {
        String encoding = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, HEADER_VALUE_BASIC + encoding);
        return headers;
    }


    public String buildUrl(String webserviceUri, String wsfunction) {
        String uri = webserviceUri.replace(PLACEHOLDER_LEARNORG_WSFUNCTION, wsfunction);
        log.debug("BuiltURL: {}", uri);
        return uri;
    }

    //public int getLeaveCount(String startDate, String endDate)
    //        throws ParseException {
    //
    //    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    //    LocalDate firstDate = LocalDate.parse(startDate, formatter);
    //    LocalDate secondDate = LocalDate.parse(endDate, formatter);
    //    Period period = Period.between(firstDate, secondDate);
    //    int diff = period.getDays();
    //    log.debug("diff {} ", diff);
    //    return diff;
    //}

    public int getWorkingDaysBetweenTwoDates(String firstDate, String secondDate) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = formatter.parse(firstDate);
        Date endDate = formatter.parse(secondDate);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 1 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 1;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays + 1;
    }
}




