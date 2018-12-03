package com.kajan.iworkflows.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class MoodleCalendarServiceTest {

    @Autowired MoodleCalendarService moodleCalendarService;

    @Test
    public void getNumberOfEvents() {
        Assert.assertEquals(1, moodleCalendarService.getNumberOfEvents(6, 12, 2018, "kajan"));
    }

    @Test
    public void stringToLocalDaete() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        LocalDate dateTime = LocalDate.parse("12/04/2018", formatter);
        Assert.assertEquals(4, dateTime.getDayOfMonth());
    }

}