package com.kunmi.taskManager.service;

import com.kunmi.taskManager.command.Command;
import com.kunmi.taskManager.command.HelpCommand;
import com.kunmi.taskManager.command.LoginCommand;
import com.kunmi.taskManager.command.RegistrationCommand;
import com.kunmi.taskManager.repository.CommandRepository;

public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandPersistence;

    public CommandServiceImpl(UserService userService, CommandRepository commandPersistence) {
        this.commandPersistence = commandPersistence;
        registerCommand(new HelpCommand());
        registerCommand(new RegistrationCommand(userService));
        registerCommand(new LoginCommand(userService));
    }

    public void registerCommand(Command command) {
            commandPersistence.saveCommand(command);
    }

    public void executeCommand(String commandName) {
        Command command = commandPersistence.getCommand(commandName);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid command:" + commandName);
        }
    }

}
