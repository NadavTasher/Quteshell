/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Shell;

@Elevation
@Help.Description("Closes the shell and disconnects.")
public class Exit extends Command {

    public Exit(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        shell.finish();
    }
}
