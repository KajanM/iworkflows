package com.kajan.iworkflows.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kajan.iworkflows.model.moodle.Event;
import com.kajan.iworkflows.model.moodle.Events;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MoodleServiceImplTest {

    @Test
    public void testStringToJson() {
        String json = "{\n" +
                "    \"events\": [\n" +
                "        {\n" +
                "            \"id\": 2,\n" +
                "            \"name\": \"Test Event\",\n" +
                "            \"description\": \"\",\n" +
                "            \"descriptionformat\": 1,\n" +
                "            \"categoryid\": 0,\n" +
                "            \"groupid\": null,\n" +
                "            \"userid\": 8,\n" +
                "            \"repeatid\": null,\n" +
                "            \"eventcount\": null,\n" +
                "            \"modulename\": \"\",\n" +
                "            \"instance\": null,\n" +
                "            \"eventtype\": \"course\",\n" +
                "            \"timestart\": 1544102160,\n" +
                "            \"timeduration\": 0,\n" +
                "            \"timesort\": 1544102160,\n" +
                "            \"visible\": 1,\n" +
                "            \"timemodified\": 1543843019,\n" +
                "            \"icon\": {\n" +
                "                \"key\": \"i/courseevent\",\n" +
                "                \"component\": \"core\",\n" +
                "                \"alttext\": \"Course event\"\n" +
                "            },\n" +
                "            \"course\": {\n" +
                "                \"id\": 5,\n" +
                "                \"fullname\": \"Computer Security\",\n" +
                "                \"shortname\": \"CS3962\",\n" +
                "                \"idnumber\": \"\",\n" +
                "                \"summary\": \"Test course 1<br />\\nLorem ipsum dolor sit amet, consectetuer adipiscing elit. Nulla non arcu lacinia neque faucibus fringilla. Vivamus porttitor turpis ac leo. Integer in sapien. Nullam eget nisl. Aliquam erat volutpat. Cras elementum. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Integer malesuada. Nullam lectus justo, vulputate eget mollis sed, tempor sed magna. Mauris elementum mauris vitae tortor. Aliquam erat volutpat.<br />\\nTemporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Pellentesque ipsum. Cras pede libero, dapibus nec, pretium sit amet, tempor quis. Aliquam ante. Proin in tellus sit amet nibh dignissim sagittis. Vivamus porttitor turpis ac leo. Duis bibendum, lectus ut viverra rhoncus, dolor nunc faucibus libero, eget facilisis enim ipsum id lacus. In sem justo, commodo ut, suscipit at, pharetra vitae, orci. Aliquam erat volutpat. Nulla est.<br />\\nVivamus luctus egestas leo. Aenean fermentum risus id tortor. Mauris dictum facilisis augue. Aliquam erat volutpat. Aliquam ornare wisi eu metus. Aliquam id dolor. Duis condimentum augue id magna semper rutrum. Donec iaculis gravida nulla. Pellentesque ipsum. Etiam dictum tincidunt diam. Quisque tincidunt scelerisque libero. Etiam egestas wisi a erat.<br />\\nInteger lacinia. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Mauris tincidunt sem sed arcu. Nullam feugiat, turpis at pulvinar vulputate, erat libero tristique tellus, nec bibendum odio risus sit amet ante. Aliquam id dolor. Maecenas sollicitudin. Et harum quidem rerum facilis est et expedita distinctio. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Nullam dapibus fermentum ipsum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Pellentesque sapien. Duis risus. Mauris elementum mauris vitae tortor. Suspendisse nisl. Integer rutrum, orci vestibulum ullamcorper ultricies, lacus quam ultricies odio, vitae placerat pede sem sit amet enim.<br />\\nIn laoreet, magna id viverra tincidunt, sem odio bibendum justo, vel imperdiet sapien wisi sed libero. Proin pede metus, vulputate nec, fermentum fringilla, vehicula vitae, justo. Nullam justo enim, consectetuer nec, ullamcorper ac, vestibulum in, elit. Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? Maecenas lorem. Etiam posuere lacus quis dolor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Curabitur ligula sapien, pulvinar a vestibulum quis, facilisis vel sapien. Nam sed tellus id magna elementum tincidunt. Suspendisse nisl. Vivamus luctus egestas leo. Nulla non arcu lacinia neque faucibus fringilla. Etiam dui sem, fermentum vitae, sagittis id, malesuada in, quam. Etiam dictum tincidunt diam. Etiam commodo dui eget wisi. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Proin pede metus, vulputate nec, fermentum fringilla, vehicula vitae, justo. Duis ante orci, molestie vitae vehicula venenatis, tincidunt ac pede. Pellentesque sapien.\",\n" +
                "                \"summaryformat\": 1,\n" +
                "                \"startdate\": 1543775400,\n" +
                "                \"enddate\": 0,\n" +
                "                \"fullnamedisplay\": \"Computer Security\",\n" +
                "                \"viewurl\": \"http://iworkflows.projects.mrt.ac.lk/moodle/course/view.php?id=5\"\n" +
                "            },\n" +
                "            \"subscription\": {\n" +
                "                \"displayeventsource\": false\n" +
                "            },\n" +
                "            \"canedit\": true,\n" +
                "            \"candelete\": true,\n" +
                "            \"deleteurl\": \"http://iworkflows.projects.mrt.ac.lk/moodle/calendar/delete.php?id=2&course=5\",\n" +
                "            \"editurl\": \"http://iworkflows.projects.mrt.ac.lk/moodle/calendar/event.php?action=edit&id=2&course=5\",\n" +
                "            \"viewurl\": \"http://iworkflows.projects.mrt.ac.lk/moodle/calendar/view.php?view=day&course=5&time=1544102160#event_2\",\n" +
                "            \"formattedtime\": \"<a href=\\\"http://iworkflows.projects.mrt.ac.lk/moodle/calendar/view.php?view=day&amp;time=1544102160\\\">Thursday, 6 December</a>, 6:46 PM\",\n" +
                "            \"isactionevent\": false,\n" +
                "            \"iscourseevent\": true,\n" +
                "            \"iscategoryevent\": false,\n" +
                "            \"groupname\": null,\n" +
                "            \"url\": \"http://iworkflows.projects.mrt.ac.lk/moodle/course/view.php?id=5\",\n" +
                "            \"islastday\": true,\n" +
                "            \"calendareventtype\": \"course\",\n" +
                "            \"popupname\": \"CS3962: Test Event\",\n" +
                "            \"draggable\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"defaulteventcontext\": 2,\n" +
                "    \"filter_selector\": \"<label class=\\\"m-r-1\\\" for=\\\"course\\\">Day view for:</label><select class=\\\"select custom-select cal_courses_flt\\\" id=\\\"menucourse\\\" name=\\\"course\\\"><option selected=\\\"selected\\\" value=\\\"1\\\">All courses</option><option value=\\\"5\\\">CS3962</option></select>\",\n" +
                "    \"courseid\": 1,\n" +
                "    \"categoryid\": 0,\n" +
                "    \"neweventtimestamp\": 1544105947,\n" +
                "    \"date\": {\n" +
                "        \"seconds\": 0,\n" +
                "        \"minutes\": 0,\n" +
                "        \"hours\": 0,\n" +
                "        \"mday\": 6,\n" +
                "        \"wday\": 4,\n" +
                "        \"mon\": 12,\n" +
                "        \"year\": 2018,\n" +
                "        \"yday\": 339,\n" +
                "        \"weekday\": \"Thursday\",\n" +
                "        \"month\": \"December\",\n" +
                "        \"timestamp\": 1544034600\n" +
                "    },\n" +
                "    \"periodname\": \"Thursday, 6 December 2018\",\n" +
                "    \"previousperiod\": {\n" +
                "        \"seconds\": 0,\n" +
                "        \"minutes\": 0,\n" +
                "        \"hours\": 0,\n" +
                "        \"mday\": 5,\n" +
                "        \"wday\": 3,\n" +
                "        \"mon\": 12,\n" +
                "        \"year\": 2018,\n" +
                "        \"yday\": 338,\n" +
                "        \"weekday\": \"Wednesday\",\n" +
                "        \"month\": \"December\",\n" +
                "        \"timestamp\": 1543948200\n" +
                "    },\n" +
                "    \"previousperiodlink\": \"http://iworkflows.projects.mrt.ac.lk/moodle/calendar/view.php?view=day&time=1543948200\",\n" +
                "    \"previousperiodname\": \"Wednesday\",\n" +
                "    \"nextperiod\": {\n" +
                "        \"seconds\": 0,\n" +
                "        \"minutes\": 0,\n" +
                "        \"hours\": 0,\n" +
                "        \"mday\": 7,\n" +
                "        \"wday\": 5,\n" +
                "        \"mon\": 12,\n" +
                "        \"year\": 2018,\n" +
                "        \"yday\": 340,\n" +
                "        \"weekday\": \"Friday\",\n" +
                "        \"month\": \"December\",\n" +
                "        \"timestamp\": 1544121000\n" +
                "    },\n" +
                "    \"nextperiodname\": \"Friday\",\n" +
                "    \"nextperiodlink\": \"http://iworkflows.projects.mrt.ac.lk/moodle/calendar/view.php?view=day&time=1544121000\",\n" +
                "    \"larrow\": \"&#x25C4;\",\n" +
                "    \"rarrow\": \"&#x25BA;\"\n" +
                "}";

        //JsonObject jsonObject = new JsonObject(json);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Events events = objectMapper.readValue(json, Events.class);
            List<Event> list = events.getEvents();
            Assert.assertEquals(1, events.getEvents().size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}