package com.kajan.iworkflows.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
public class CompletedTaskStore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String taskId;
    private String principal;
    private String owner;
    private String submittedDate;
    private String status;

    public CompletedTaskStore (String taskId, String principal, String owner, String submittedDate, String status){
        this.taskId = taskId;
        this.principal = principal;
        this.owner = owner;
        this.submittedDate = submittedDate;
        this.status = status;

    }
}
