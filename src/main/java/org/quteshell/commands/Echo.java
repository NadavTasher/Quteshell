/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Shell;

@Elevation(Elevation.DEFAULT)
@Help.Description("Prints the input.")
public class Echo extends Command {

    public Echo(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        if (arguments != null) {
            shell.writeln(arguments);
        }
    }
}
