package com.kunmi.taskManager.project;

public interface ProjectServices {
    Project create(Project project);
    Project update(Project project);
    void view(Project project);
    void delete(Project project);

}
