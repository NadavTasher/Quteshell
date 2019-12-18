/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Shell;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

@Elevation(Elevation.ALL)
@Help.Description("Shows information about commands.")
public class Help extends Command {

    private static final int COLUMNS = 3;

    public Help(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        ArrayList<Command> commands = shell.getCommands();
        if (arguments == null) {
            shell.writeln("List of commands:");
            for (int c = 0; c < commands.size(); c += COLUMNS) {
                for (int r = 0; r < COLUMNS; r++) {
                    if (c + r < commands.size()) {
                        shell.write(Shell.Configuration.Commands.getName(commands.get(c + r)));
                    }
                    shell.write("\t\t");
                }
                shell.writeln();
            }
            shell.writeln("Type 'help [command]' for more help.");
        } else {
            Command help = null;
            for (Command command : commands) {
                if (Shell.Configuration.Commands.getName(command).equals(arguments)) {
                    help = command;
                    break;
                }
            }
            if (help != null) {
                String text = null;
                for (Annotation annotation : help.getClass().getAnnotations()) {
                    if (annotation instanceof Description) {
                        text = ((Description) annotation).value();
                    }
                }
                if (text != null) {
                    shell.writeln("Showing help for '" + arguments + "':");
                    shell.writeln(text);
                } else {
                    shell.writeln("No description available for '" + arguments + "'");
                }
            } else {
                shell.writeln("Command not found");
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Description {
        String value();
    }
}
