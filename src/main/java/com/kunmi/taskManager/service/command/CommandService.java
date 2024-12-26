package com.kunmi.taskManager.service.command;

public interface CommandService {
    void executeCommand(String commandName);
    void registerCommand(Command command);

}
