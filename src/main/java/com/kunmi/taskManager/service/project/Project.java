package com.kunmi.taskManager.service.project;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Project {

    private String id;
    private String name;
    private LocalDateTime createDate;
    private String userId;
}
