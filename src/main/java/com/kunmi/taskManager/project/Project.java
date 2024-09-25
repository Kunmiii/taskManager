package com.kunmi.taskManager.project;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Project {

    private int id;
    private String name;
    private Date createDate;
}
