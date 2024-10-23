package com.kunmi.taskManager.service;

import com.kunmi.taskManager.command.*;
import com.kunmi.taskManager.repository.CommandRepositoryImpl;

public class CommandServiceImpl {

    private final CommandRepositoryImpl commandPersistence;

    public CommandServiceImpl(UserService userService, CommandRepositoryImpl commandPersistence) {
        this.commandPersistence = commandPersistence;
        registerCommand(new HelpCommand());
        registerCommand(new RegistrationCommand(userService));
        registerCommand(new LoginCommand(userService));
    }

    public void registerCommand(CommandService command) {
            commandPersistence.saveCommand(command);
    }

    public void executeCommand(String commandName) {
        CommandService command = commandPersistence.getCommand(commandName);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid command:" + commandName);
        }
    }

}
