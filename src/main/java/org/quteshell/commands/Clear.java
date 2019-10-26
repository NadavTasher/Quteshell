/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Quteshell;

@Elevation(Elevation.DEFAULT)
@Help.Description("The clear command clears the client's terminal.")
public class Clear implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.clearAll();
    }
}
