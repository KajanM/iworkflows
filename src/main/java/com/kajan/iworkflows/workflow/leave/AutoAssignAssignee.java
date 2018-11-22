package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.model.Approver;
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
import static com.kajan.iworkflows.util.Constants.PLACEHOLDER_LEARNORG_WSFUNCTION;
import static com.kajan.iworkflows.util.WorkflowConstants.*;


@Service("autoAssignAssignee")
@Slf4j
public class AutoAssignAssignee implements JavaDelegate {

    private final GroupMapperServiceImpl mapService;
    private final RestTemplate restTemplate;
    private final LearnOrgServiceImpl learnOrgService;
    private final String webserviceUri;
    private final String wsfunction;

    @Value("${testing}")
    private Boolean testing;

    @Autowired
    public AutoAssignAssignee(GroupMapperServiceImpl mapService, RestTemplate restTemplate,
                              LearnOrgServiceImpl learnOrgService,
                              @Value("${learnorg.uri.system}") String webserviceUri,
                              @Value("${learnorg.wsfunction.get-department-head}") String wsfunction) {
        this.mapService = mapService;
        this.restTemplate = restTemplate;
        this.webserviceUri = webserviceUri;
        this.learnOrgService = learnOrgService;
        this.wsfunction = wsfunction;
    }

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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

        String head = null;
        String clerk = null;
        if (testing) {
            //since learnorg can only be acccessed via uni wifi :(
            head = "kajan";
            clerk = "kirisanth";
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

                String url = buildUrl(role, wsfunction);
                log.debug("url : " + url);

                HttpEntity<String> request = new HttpEntity<>("", learnOrgService.getLearnOrgHeadersAsIworkflows());
                ResponseEntity<Approver> response = restTemplate.postForEntity(url, request, Approver.class);
                log.debug("Response ---------" + response.getBody());

                clerk = response.getBody().getClerk();
                head = response.getBody().getHead();
                break;
            }
        }
        log.debug("Auto assigning the task to HOD {}", head);
        log.debug("Auto assigning the task to clerk {}", clerk);
        execution.setVariable(HEAD_APPROVER_KEY, head);
        execution.setVariable(CLERK_APPROVER_KEY, clerk);


    }

    public String buildUrl(String role, String wsfunction) {
        String uri = webserviceUri.replace(PLACEHOLDER_LEARNORG_DEPARTMENT, role)
                .replace(PLACEHOLDER_LEARNORG_WSFUNCTION, wsfunction);
        log.debug("BuiltURL: {}", uri);
        return uri;
    }

}
