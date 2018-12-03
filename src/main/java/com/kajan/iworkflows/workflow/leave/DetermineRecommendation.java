package com.kajan.iworkflows.workflow.leave;

import com.kajan.iworkflows.service.MoodleCalendarService;
import com.kajan.iworkflows.service.impl.LearnOrgServiceImpl;
import com.kajan.iworkflows.workflow.dto.SubmittedLeaveFormDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.kajan.iworkflows.util.WorkflowConstants.*;

@Service("determineRecommendation")
@Slf4j
public class DetermineRecommendation implements JavaDelegate {

    private final LearnOrgServiceImpl learnOrgService;
    private final MoodleCalendarService moodleCalendarService;

    @Autowired
    public DetermineRecommendation(LearnOrgServiceImpl learnOrgService,
                                   MoodleCalendarService moodleCalendarService) {
        this.learnOrgService = learnOrgService;
        this.moodleCalendarService = moodleCalendarService;
    }

    @Override
    public void execute(DelegateExecution execution) throws ParseException {
        SubmittedLeaveFormDetails submittedLeaveFormDetails = (SubmittedLeaveFormDetails) execution.getVariable(LEAVE_DETAILS_KEY);
        String leaveType = submittedLeaveFormDetails.getLeaveType();
        int leaveAppliedFor = learnOrgService.getWorkingDaysBetweenTwoDates(submittedLeaveFormDetails.getStartDate(), submittedLeaveFormDetails.getEndDate());
        String employeeId = submittedLeaveFormDetails.getEmployeeId();
        execution.setVariable("employeeId", employeeId);
        execution.setVariable("leaveType", leaveType);
        execution.setVariable("leaveAppliedFor", leaveAppliedFor);
        log.debug("no leave applied for {} ", leaveAppliedFor);
        int remainingLeave = submittedLeaveFormDetails.getRemainingLeavesByLeaveType(leaveType);
        log.debug("remaining {}, leaves {} ", leaveType, remainingLeave);
        log.debug("submitted leave details {} ", submittedLeaveFormDetails);

        String principal = (String) execution.getVariable(OWNER_KEY);

        int numberOfEventsToCancel = getNumberOfEventsToCancel(submittedLeaveFormDetails.getStartDate(),
                submittedLeaveFormDetails.getEndDate(),
                principal);

        log.debug("number of events to cancel = {}", numberOfEventsToCancel);


        if (remainingLeave < leaveAppliedFor) {
            execution.setVariable(RECOMMENDATION_KEY, "not_recommended");
        } else if(numberOfEventsToCancel > 2) {
            execution.setVariable(RECOMMENDATION_KEY, "not_recommended");
        } else {
            execution.setVariable(RECOMMENDATION_KEY, "recommended");
        }

    }

    private int getNumberOfEventsToCancel(String startDateStr, String endDateStr, String principal) {
        int numberOfEventsToBeCancelled = 0;
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
        {
            numberOfEventsToBeCancelled += moodleCalendarService.getNumberOfEvents(date.getDayOfMonth(), date.getMonthValue(), date.getYear(), principal);
        }
        return numberOfEventsToBeCancelled;
    }
}
