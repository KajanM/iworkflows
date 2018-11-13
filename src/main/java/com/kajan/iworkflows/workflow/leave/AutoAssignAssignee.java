package com.kajan.iworkflows.workflow.leave;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kajan.iworkflows.model.Mapper;
import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.model.Mapper;
import com.kajan.iworkflows.model.TokenStore;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.service.impl.MapperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_FILE_PATH;
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_USERID;

import static com.kajan.iworkflows.util.Constants.TokenProvider.LEARNORG;
@Service("autoAssignAssignee")
@Slf4j
public class AutoAssignAssignee implements JavaDelegate  {

    private final MapperServiceImpl mapService;

    @Autowired
    public AutoAssignAssignee(MapperServiceImpl mapService) {
        this.mapService = mapService;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OauthTokenService oauthTokenService;

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try{
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // owner is the one who submitted the leave request
        String owner = (String) execution.getVariable("owner");
        log.debug("Owner of the tasks is {}", owner);

        // TODO: Ramiya, logic to find out the approver goes here
        Collection<? extends GrantedAuthority> groups = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.debug("task owner's groups : " + groups);

        List<Mapper> userStoreList = new ArrayList<>();
        log.debug("role : " + mapService.findByIworkflowsRole("ROLE_CSE").toString());
        mapService.findByIworkflowsRole("ROLE_CSE").forEach(userStoreList::add);
        Mapper userStore = userStoreList.get(0);

        String role = userStore.getLearnorgRole();
        log.debug("learnorg department : "+ role);

        Principal principal = (Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TokenDTO tokenDTO = oauthTokenService.getToken(principal, LEARNORG);
        String accesstoken = tokenDTO.getAccessToken().getValue();
        log.debug("Access Token : " + accesstoken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("access_token", accesstoken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        String access_token_url = "https://10.8.90.4/oauth/iworkflows.php?department="+ role;
        ResponseEntity<String> respons = restTemplate.postForEntity( access_token_url, request , String.class );
        log.debug("Response ---------" + respons.getBody());

        // Get the appprover From the recieved JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(respons.getBody());
        String approver = node.path("Department-Head").asText();
        log.debug("Auto assigning the task to {}", approver);

        execution.setVariable("approver", approver);
    }
}
