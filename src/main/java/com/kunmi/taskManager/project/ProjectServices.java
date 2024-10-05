package com.kunmi.taskManager.project;

public interface ProjectServices {
    Project create(Project project);
    Project update(Project project);
    void view(int id);
    void delete(int id);

}
