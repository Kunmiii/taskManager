package com.kunmi.taskManager.repository.commandRepo;

import com.kunmi.taskManager.service.command.Command;

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
