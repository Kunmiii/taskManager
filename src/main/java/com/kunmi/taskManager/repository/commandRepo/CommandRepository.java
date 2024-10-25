package com.kunmi.taskManager.repository.commandRepo;

import com.kunmi.taskManager.service.command.Command;

public interface CommandRepository {
    void saveCommand(Command command);
    Command getCommand(String commandName);
}
