package org.quteshell.commands;

import org.quteshell.command.Elevation;
import org.quteshell.Quteshell;
import org.quteshell.command.Command;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Elevation(Elevation.ALL)
@Help.Description("The help command lists all commands, or a command description.")
public class Help implements Command {

    private static final int COLUMNS = 3;

    @Override
    public void execute(Quteshell shell, String arguments) {
        ArrayList<Command> commands = shell.getCommands();
        if (arguments == null) {
            shell.writeln("List of available commands:");
            for (int c = 0; c < commands.size(); c += COLUMNS) {
                for (int r = 0; r < COLUMNS; r++) {
                    if (c + r < commands.size()) {
                        shell.write(Quteshell.Commands.getName(commands.get(c + r)));
                    }
                    shell.write("\t\t");
                }
                shell.writeln();
            }
        } else {
            shell.writeln(Quteshell.Commands.getName(this) + " - '" + arguments + "'");
            Command help = null;
            for (Command command : commands) {
                if (Quteshell.Commands.getName(command).equals(arguments)) {
                    help = command;
                    break;
                }
            }
            String text;
            if (help != null) {
                text = "No description available";
                for (Annotation annotation : help.getClass().getAnnotations()) {
                    if (annotation instanceof Description) {
                        text = ((Description) annotation).value();
                    }
                }
            } else {
                text = "Command not found";
            }
            shell.writeln(text);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Description {
        String value();
    }
}
