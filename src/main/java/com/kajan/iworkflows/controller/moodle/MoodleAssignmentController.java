package com.kajan.iworkflows.controller.moodle;

import com.kajan.iworkflows.service.MoodleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/moodle")
public class MoodleAssignmentController {

    private final MoodleService moodleService;
    private final String FUNCTION_GET_COURSES_BY_FIELD;
    private final String FUNCTION_GET_ASSIGNMENTS;

    @Autowired
    public MoodleAssignmentController(MoodleService moodleService,
                                      @Value("${moodle.wsfunction.get-courses-by-field}") String wsfunctionGetCoursesByField,
                                      @Value("${moodle.wsfunction.get-assignments}") String wsfunctionGetAssignments) {
        this.moodleService = moodleService;
        FUNCTION_GET_COURSES_BY_FIELD = wsfunctionGetCoursesByField;
        FUNCTION_GET_ASSIGNMENTS = wsfunctionGetAssignments;
    }

    @GetMapping("/courses")
    public ResponseEntity getCourseDetails(Principal principal) {
        return moodleService.executeWsFunction(FUNCTION_GET_COURSES_BY_FIELD, HttpMethod.POST, String.class, principal);
    }

    @GetMapping("/assignments")
    public ResponseEntity getAssignementDetails(Principal principal) {
        return moodleService.executeWsFunction(FUNCTION_GET_ASSIGNMENTS, HttpMethod.GET, String.class, principal);
    }
}
