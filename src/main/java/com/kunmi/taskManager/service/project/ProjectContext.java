package com.kunmi.taskManager.service.project;

public class ProjectContext {

    private static final ThreadLocal<Project> projectThreadLocal = new ThreadLocal<>();

    public static void setCurrentProject(Project project) {
        projectThreadLocal.set(project);
    }

    public static Project getCurrentProject() {
        return projectThreadLocal.get();
    }

    public static void removeCurrentProject() {
        projectThreadLocal.remove();
    }
}
