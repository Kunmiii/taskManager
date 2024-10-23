package com.kunmi.taskManager.user;

import com.kunmi.taskManager.project.Project;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString

public class User implements IUser {

    private final String id;
    private final String name;
    private final String lastName;
    private final String password;
    private final String email;
    private final List<Project> projects;

    public User(String id, String name, String lastName, String password, String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.projects = new ArrayList<>();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void addProject(Project project) {
        this.projects.add(project);
    }
}
