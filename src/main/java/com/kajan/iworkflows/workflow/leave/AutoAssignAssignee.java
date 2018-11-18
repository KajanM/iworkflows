package com.kajan.iworkflows.workflow.leave;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kajan.iworkflows.model.GroupMapper;
import com.kajan.iworkflows.service.impl.GroupMapperServiceImpl;
import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_LEARNORG_DEPARTMENT;
import static com.kajan.iworkflows.util.WorkflowConstants.APPROVER_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.OWNER_KEY;


@Service("autoAssignAssignee")
@Slf4j
public class AutoAssignAssignee implements JavaDelegate {

    private final GroupMapperServiceImpl mapService;
    private final RestTemplate restTemplate;
    private final LearnOrgServiceImpl learnOrgService;
    private final String webserviceUri;

    @Value("${testing}")
    private Boolean testing;

    @Autowired
    public AutoAssignAssignee(GroupMapperServiceImpl mapService, RestTemplate restTemplate,
                              LearnOrgServiceImpl learnOrgService,
                              @Value("${learnorg.uri.system}") String webserviceUri) {
        this.mapService = mapService;
        this.restTemplate = restTemplate;
        this.webserviceUri = webserviceUri;
        this.learnOrgService = learnOrgService;
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
        String owner = (String) execution.getVariable(OWNER_KEY);
        log.debug("Owner of the tasks is {}", owner);

        String approver = null;

        if(testing) {
            //since learnorg can only be acccessed via uni wifi :(
            approver = "kajan";
        } else {
            Collection<? extends GrantedAuthority> groups = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            log.debug("task owner's groups : " + groups);

            List<GroupMapper> userStoreList = new ArrayList<>();
            GroupMapper userStore;
            String role = null;
            for (GrantedAuthority group : groups) {
                if (!(mapService.findByIworkflowsRole(group.toString())).iterator().hasNext()) {
                    continue;
                }
                mapService.findByIworkflowsRole(group.toString()).forEach(userStoreList::add);
                userStore = userStoreList.get(0);
                role = userStore.getLearnorgRole();
                log.debug("learnorg department : " + role);

                String url = buildUrl(role);
                log.debug("url : " + url);

                HttpEntity<String> request = new HttpEntity<>("", learnOrgService.getLearnOrgHeadersAsIworkflows());
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                log.debug("Response ---------" + response.getBody());

//            TokenDTO tokenDTO = oauthTokenService.getToken(SYSTEM_KEY, LEARNORG);
//            String accesstoken = tokenDTO.getAccessToken().getValue();
//            log.debug("Access Token : " + accesstoken);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
//            map.add("access_token", accesstoken);
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//            ResponseEntity<String> response = this.restTemplate.postForEntity( url, request , String.class );
//            log.debug("Response ---------" + response.getBody());

                // Get the appprover From the recieved JSON response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(response.getBody());
                approver = node.path("Department-Head").asText();
                break;
            }
        }
        log.debug("Auto assigning the task to {}", approver);
        execution.setVariable(APPROVER_KEY, approver);

    }

    private String buildUrl(String role) {
        String uri = webserviceUri.replace(PLACEHOLDER_LEARNORG_DEPARTMENT, role);
        log.debug("BuiltURL: {}", uri);
        return uri;
    }
}
