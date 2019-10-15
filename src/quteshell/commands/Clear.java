package quteshell.commands;

import quteshell.command.Command;
import quteshell.Quteshell;
import quteshell.command.Elevation;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Elevation(Elevation.DEFAULT)
@Help.Description("The clear command clears the client's terminal.")
public class Clear implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.clear();
    }
}
