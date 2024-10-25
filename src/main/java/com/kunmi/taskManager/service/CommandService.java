package com.kunmi.taskManager.service;

import com.kunmi.taskManager.command.Command;

public interface CommandService {
    void executeCommand(String commandName);
    void registerCommand(Command command);

}
