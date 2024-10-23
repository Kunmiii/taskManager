package com.kunmi.taskManager.persistence;

import com.kunmi.taskManager.service.CommandService;

public interface CommandPersistence {
    void saveCommand(CommandService command);
    CommandService getCommand(String commandName);
}
