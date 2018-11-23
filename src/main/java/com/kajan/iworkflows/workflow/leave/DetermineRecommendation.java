package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.text.ParseException;

import static com.kajan.iworkflows.util.WorkflowConstants.LEAVE_DETAILS_KEY;
import static com.kajan.iworkflows.util.WorkflowConstants.RECOMMENDATION_KEY;

@Service("determineRecommendation")
@Slf4j
public class DetermineRecommendation implements JavaDelegate {

    private final LearnOrgServiceImpl learnOrgService;

    public DetermineRecommendation(LearnOrgServiceImpl learnOrgService) {
        this.learnOrgService = learnOrgService;
    }

    @Override
    public void execute(DelegateExecution execution) throws ParseException {
        // TODO: add actual logic to determine recommendation

        SubmittedLeaveFormDetails submittedLeaveFormDetails = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);
        String leaveType = submittedLeaveFormDetails.getLeaveType();
        int leaveAppliedFor = learnOrgService.getWorkingDaysBetweenTwoDates(submittedLeaveFormDetails.getStartDate(), submittedLeaveFormDetails.getEndDate());
        String employeeId = submittedLeaveFormDetails.getEmployeeId();
        execution.setVariable("employeeId",employeeId);
        execution.setVariable("leaveType",leaveType);
        execution.setVariable("leaveAppliedFor", leaveAppliedFor);
        log.debug("no leave applied for {} ", leaveAppliedFor);
        int remainingLeave = submittedLeaveFormDetails.getRemainingLeavesByLeaveType(leaveType);
        log.debug("remaining leaves {} ", remainingLeave);

        if (remainingLeave < leaveAppliedFor) {
            execution.setVariable(RECOMMENDATION_KEY, "not_recommended");
        } else {
            execution.setVariable(RECOMMENDATION_KEY, "recommended");
        }

    }
}
