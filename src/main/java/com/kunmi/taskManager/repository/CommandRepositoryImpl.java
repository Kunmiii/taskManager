package com.kunmi.taskManager.repository;

import com.kunmi.taskManager.command.Command;
import com.kunmi.taskManager.service.CommandService;

import java.util.HashMap;
import java.util.Map;

public class CommandRepositoryImpl implements CommandRepository {

    private final Map<String, Command> commandList = new HashMap<>();

    @Override
    public void saveCommand(Command command) {
        commandList.put(command.getName(), command);
    }

    @Override
    public Command getCommand(String commandName) {
       return commandList.get(commandName);
    }
}
