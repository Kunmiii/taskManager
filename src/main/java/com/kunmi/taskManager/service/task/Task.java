package com.kunmi.taskManager.service.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
@Getter
@ToString
@AllArgsConstructor


public class Task {

    private int id;
    private String name;
    private String projectID;
    private Date createDate;
}
