package com.kunmi.taskManager.user;

import java.util.HashMap;
import java.util.Map;

public class CommandService {

    private UserManagement userManagement;
    private final Map<String, Command> commandList = new HashMap<>();

    public CommandService(UserManagement userManagement) {
        this.userManagement = userManagement;
        registerCommand("help", new HelpCommand());
        registerCommand("registration", new RegistrationCommand(userManagement));
        registerCommand("login", new LoginCommand(userManagement));
    }


    public void registerCommand(String commandName, Command command) {
        commandList.put(commandName, command);
    }

    public void executeCommand(String commandName) {
        Command command = commandList.get(commandName);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid command:" + commandName);
        }
    }

}
