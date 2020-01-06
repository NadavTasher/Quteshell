/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Shell;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

@Elevation
@Help.Description("Shows information about commands.")
public class Help extends Command {

    public Help(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        ArrayList<Command> commands = shell.getCommands();
        shell.writeln("List of commands:");
        for (Command command : commands) {
            if (command.getClass().isAnnotationPresent(Elevation.class)) {
                if (shell.getElevation() >= command.getClass().getAnnotation(Elevation.class).value()) {
                    // Write command name
                    shell.write(command.getClass().getSimpleName().toLowerCase(), Shell.Color.LightOrange);
                    // Write tabs
                    shell.write("\t");
                    // Write another tab is name is shorter then 8 characters
                    if (command.getClass().getSimpleName().toLowerCase().length() < 8)
                        shell.write("\t");
                    // Write command description
                    if (command.getClass().isAnnotationPresent(Description.class))
                        shell.write(command.getClass().getAnnotation(Description.class).value());
                    else
                        shell.write("No description.");
                    // Newline
                    shell.writeln();
                }
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Description {
        String value();
    }
}
