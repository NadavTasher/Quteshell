/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Elevation;
import org.quteshell.Command;
import org.quteshell.Quteshell;

@Elevation(Elevation.ALL)
@Help.Description("The exit command closes the shell and disconnects.")
public class Exit implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.finish();
    }
}
