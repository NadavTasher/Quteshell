/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Quteshell;

@Elevation(Elevation.DEFAULT)
@Help.Description("The echo command prints back what you type in.")
public class Echo implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        if (arguments != null) {
            shell.writeln(arguments);
        } else {
            shell.writeln();
        }
    }
}
