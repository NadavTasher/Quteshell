/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Shell;

@Elevation(Elevation.DEFAULT)
@Help.Description("Clears the client's terminal.")
public class Clear extends Command {

    public Clear(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        shell.clearAll();
    }
}
