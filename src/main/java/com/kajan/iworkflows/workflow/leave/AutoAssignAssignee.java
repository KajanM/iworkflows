package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.model.Approver;
import com.kajan.iworkflows.model.GroupMapper;
import com.kajan.iworkflows.model.LogStore;
import com.kajan.iworkflows.repository.LogStoreRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.kajan.iworkflows.util.WorkflowConstants.*;


@Service("autoAssignAssignee")
@Slf4j
public class AutoAssignAssignee implements JavaDelegate {

    private final GroupMapperServiceImpl mapService;
    private final RestTemplate restTemplate;
    private final LearnOrgServiceImpl learnOrgService;
    private final String webserviceUri;
    private final String wsfunction;
    private final LogStoreRepository logStoreRepository;
    private Timestamp timestamp;

    @Value("${testing}")
    private Boolean testing;

    @Autowired
    public AutoAssignAssignee(GroupMapperServiceImpl mapService, RestTemplate restTemplate,
                              LearnOrgServiceImpl learnOrgService,
                              @Value("${learnorg.uri.system}") String webserviceUri,
                              @Value("${learnorg.wsfunction.get-department-head}") String wsfunction,
                              LogStoreRepository logStoreRepository) {
        this.mapService = mapService;
        this.restTemplate = restTemplate;
        this.webserviceUri = webserviceUri;
        this.learnOrgService = learnOrgService;
        this.wsfunction = wsfunction;
        this.logStoreRepository = logStoreRepository;
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
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(owner, timestamp, "Owner of the tasks is " + owner));

        String head = null;
        String clerk = null;
        String role = null;
        if (testing) {
            //since learnorg can only be acccessed via uni wifi :(
            head = "kajan";
            clerk = "kirisanth";
        } else {
            Collection<? extends GrantedAuthority> groups = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            log.debug("task owner's groups {}", groups);
            timestamp = new Timestamp(System.currentTimeMillis());
            logStoreRepository.save(new LogStore(owner, timestamp, "task owner's groups " + groups));

            List<GroupMapper> userStoreList = new ArrayList<>();
            GroupMapper userStore;

            for (GrantedAuthority group : groups) {
                if (!(mapService.findByIworkflowsRole(group.toString())).iterator().hasNext()) {
                    continue;
                }
                mapService.findByIworkflowsRole(group.toString()).forEach(userStoreList::add);
                userStore = userStoreList.get(0);
                role = userStore.getLearnorgRole();
                log.debug("Requestor {} belongs to {} department in learnorg", owner, role);
                timestamp = new Timestamp(System.currentTimeMillis());
                logStoreRepository.save(new LogStore(owner, timestamp, "Requestor " + owner + " belongs to " + groups + " department in learnorg"));

                String url = learnOrgService.buildUrl(webserviceUri, wsfunction);
                log.debug("url : {} ", url);

                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("role", role);

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, learnOrgService.getLearnOrgHeadersAsIworkflows());
                ResponseEntity<Approver> response = restTemplate.postForEntity(url, request, Approver.class);
                log.debug("Response from LearnOrg ---------" + response.getBody());
                timestamp = new Timestamp(System.currentTimeMillis());
                logStoreRepository.save(new LogStore(owner, timestamp, "Response from LearnOrg --------- " + response.getBody()));

                clerk = response.getBody().getClerk();
                head = response.getBody().getHead();

                log.debug("Leave request of {} is assigned to {} hod : {} and clerk {}", owner, role, head, clerk);
                timestamp = new Timestamp(System.currentTimeMillis());
                logStoreRepository.save(new LogStore(owner, timestamp, "Leave request of " + owner + " is assigned to " + " hod :  " + head + " and clerk :" + clerk));
                break;
            }
        }
        log.debug("Leave request of {} is assigned to {} hod : {} and clerk {}", owner, role, head, clerk);
        timestamp = new Timestamp(System.currentTimeMillis());
        logStoreRepository.save(new LogStore(owner, timestamp, "Leave request of " + owner + " is assigned to " + " hod :  " + head + " and clerk :" + clerk));
        execution.setVariable(HEAD_APPROVER_KEY, head);
        execution.setVariable(CLERK_APPROVER_KEY, clerk);
        logStoreRepository.save(new LogStore(head, timestamp, "Leave request of " + owner + " is assigned to " + " hod :  " + head));
        logStoreRepository.save(new LogStore(clerk, timestamp, "Leave request of " + owner + " is assigned to " + " clerk :  " + clerk));


    }

    //public String buildUrl(String role, String wsfunction) {
    //    String uri = webserviceUri
    //            .replace(PLACEHOLDER_LEARNORG_WSFUNCTION, wsfunction);
    //    log.debug("BuiltURL: {}", uri);
    //    return uri;
    //}

}
