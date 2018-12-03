package com.kajan.iworkflows.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
public class LogStore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String principal;
    private Timestamp timestamp;
    @Column(columnDefinition = "text")
    private String message;

    public LogStore() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public LogStore(String principal, Timestamp timestamp, String message) {
        this();
        this.principal = principal;
        this.timestamp = timestamp;
        this.message = message;
    }

}
