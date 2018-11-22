package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_TYPE_KEY;

@Service("leavePostApprovalService")
@Slf4j
public class LeavePostApprovalService implements JavaDelegate {

    private final RestTemplate restTemplate;
    private final LearnOrgServiceImpl learnOrgService;
    private final String webserviceUri;
    private final String wsfunction;

    @Autowired
    public LeavePostApprovalService(RestTemplate restTemplate, LearnOrgServiceImpl learnOrgService,
                                    @Value("${learnorg.uri.system}") String webserviceUri,
                                    @Value("${learnorg.wsfunction.set-approved-leave}") String wsfunction) {
        this.learnOrgService = learnOrgService;
        this.restTemplate = restTemplate;
        this.webserviceUri = webserviceUri;
        this.wsfunction = wsfunction;

    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("Leave request approved");

        log.debug("Leave details {} ", execution.getVariable(LEAVE_DETAILS_KEY));
        SubmittedLeaveFormDetails submittedLeaveFormDetails = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);
        String employeeId = submittedLeaveFormDetails.getEmployeeId();
        String leaveType = submittedLeaveFormDetails.getLeaveType();
        int leaveAppliedFor = learnOrgService.getWorkingDaysBetweenTwoDates(submittedLeaveFormDetails.getStartDate(), submittedLeaveFormDetails.getEndDate());
        log.debug("no leave applied for {} ", leaveAppliedFor);

        String url = learnOrgService.buildUrl(webserviceUri, wsfunction);
        log.debug("url : " + url);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add(LEAVE_TYPE_KEY, leaveType);
        map.add("employeeId", employeeId);
        map.add("leaveCount", Integer.toString(leaveAppliedFor));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, learnOrgService.getLearnOrgHeadersAsIworkflows());
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        log.debug("Response ---------" + response.getBody());
    }
}
