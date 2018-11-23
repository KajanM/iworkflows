package com.kajan.iworkflows.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
}
