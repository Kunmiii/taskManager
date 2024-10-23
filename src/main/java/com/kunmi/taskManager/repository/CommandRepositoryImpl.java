package com.kunmi.taskManager.repository;

import com.kunmi.taskManager.service.CommandService;

import java.util.HashMap;
import java.util.Map;

public class CommandRepositoryImpl implements CommandRepository {

    private final Map<String, CommandService> commandList = new HashMap<>();

    @Override
    public void saveCommand(CommandService command) {
        commandList.put(command.getName(), command);
    }

    @Override
    public CommandService getCommand(String commandName) {
       return commandList.get(commandName);
    }
}
