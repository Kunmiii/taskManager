package com.kunmi.taskManager.repository;

import com.kunmi.taskManager.service.CommandService;

public interface CommandRepository {
    void saveCommand(CommandService command);
    CommandService getCommand(String commandName);
}
