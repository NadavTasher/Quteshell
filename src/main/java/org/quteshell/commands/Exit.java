package org.quteshell.commands;

import org.quteshell.command.Elevation;
import org.quteshell.command.Command;
import org.quteshell.Quteshell;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Elevation(Elevation.ALL)
@Help.Description("The exit command closes the shell and disconnects.")
public class Exit implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.finish();
    }
}
