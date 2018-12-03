package com.kajan.iworkflows.model.moodle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private int id;
    private String name;
    private String description;
    private int descriptionformat;
    private int categoryid;
    private int groupid;
    private int userid;
    private int repeatid;
    private int eventcount;
    private int modulename;
    private int instance;
    private String eventtype;
    private int timestart;
    private int timeduration;
    private int timesort;
    private int visible;
    private int timemodified;
}
