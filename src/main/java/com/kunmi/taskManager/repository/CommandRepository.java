package com.kunmi.taskManager.repository;

import com.kunmi.taskManager.command.Command;

public interface CommandRepository {
    void saveCommand(Command command);
    Command getCommand(String commandName);
}
