/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Elevation;
import org.quteshell.Shell;
import org.quteshell.Command;

@Elevation(Elevation.DEFAULT)
@Help.Description("Lists the previously ran commands.")
public class History extends Command {

    public History(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        for (int h = 0; h < shell.getHistory().size(); h++) {
            shell.write(h + "\t");
            shell.writeln(shell.getHistory().get(h));
        }
    }
}