package com.kajan.iworkflows.workflow.leave;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kajan.iworkflows.model.GroupMapper;
import com.kajan.iworkflows.dto.TokenDTO;
import com.kajan.iworkflows.service.OauthTokenService;
import com.kajan.iworkflows.service.impl.GroupMapperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

import static com.kajan.iworkflows.util.Constants.TokenProvider.LEARNORG;
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_LEARNORG_DEPARTMENT;
import static com.kajan.iworkflows.util.WorkflowConstants.APPROVER_KEY;
@Service("autoAssignAssignee")
@Slf4j
public class AutoAssignAssignee implements JavaDelegate  {

    private final GroupMapperServiceImpl mapService;
    private final RestTemplate restTemplate;
    private final OauthTokenService oauthTokenService;
    private final String webserviceUri;

    @Autowired
    public AutoAssignAssignee(GroupMapperServiceImpl mapService, RestTemplate restTemplate, OauthTokenService oauthTokenService,
                              @Value("${learnorg.uri.system}") String webserviceUri) {
        this.mapService = mapService;
        this.restTemplate = restTemplate;
        this.oauthTokenService = oauthTokenService;
        this.webserviceUri = webserviceUri;
    }

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

        Collection<? extends GrantedAuthority> groups = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.debug("task owner's groups : " + groups);

        List<GroupMapper> userStoreList = new ArrayList<>();
        GroupMapper userStore;
        String role = null;
        for (GrantedAuthority group : groups) {
            if ( !(mapService.findByIworkflowsRole(group.toString())).iterator().hasNext()){
                continue;
            }
            mapService.findByIworkflowsRole(group.toString()).forEach(userStoreList::add);
            Iterable<GroupMapper> results = mapService.findByIworkflowsRole(group.toString());
            userStore = userStoreList.get(0);
            role = userStore.getLearnorgRole();
            log.debug("learnorg department : "+ role);

            String access_token_url = buildUrl(role);
            String principal = SecurityContextHolder.getContext().getAuthentication().getName();
            log.debug("principal : "+ principal + " " + oauthTokenService.getToken(principal, LEARNORG));
            TokenDTO tokenDTO = oauthTokenService.getToken(principal, LEARNORG);
            String accesstoken = tokenDTO.getAccessToken().getValue();
            log.debug("Access Token : " + accesstoken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("access_token", accesstoken);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> respons = this.restTemplate.postForEntity( access_token_url, request , String.class );
            log.debug("Response ---------" + respons.getBody());

            // Get the appprover From the recieved JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(respons.getBody());
            String approver = node.path("Department-Head").asText();
            log.debug("Auto assigning the task to {}", approver);

            execution.setVariable(APPROVER_KEY, approver);
            break;
        }

    }

    private String buildUrl(String role) {
        String uri = webserviceUri.replace(PLACEHOLDER_LEARNORG_DEPARTMENT, role);
        log.debug("BuiltURL: {}", uri);
        return uri;
    }
}
