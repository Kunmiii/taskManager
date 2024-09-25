package com.kunmi.taskManager.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User {

    private String name;
    private String lastName;
    private String password;
    private String email;

}
