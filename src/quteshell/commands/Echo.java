package quteshell.commands;

import quteshell.command.Command;
import quteshell.Quteshell;
import quteshell.command.Elevation;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Elevation(Elevation.DEFAULT)
@Help.Description("The echo command prints back what you type in.")
public class Echo implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln(arguments);
    }
}
