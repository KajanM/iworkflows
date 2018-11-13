package com.kajan.iworkflows.workflow.leave;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import static com.kajan.iworkflows.util.WorkflowConstants.RECOMMENDATION_KEY;

@Service("determineRecommendation")
@Slf4j
public class DetermineRecommendation implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // TODO: add actual logic to determine recommendation
        execution.setVariable(RECOMMENDATION_KEY, "recommended");
    }
}
