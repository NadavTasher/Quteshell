/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.Shell;

@Elevation(Elevation.DEFAULT)
@Help.Description("Displays a welcome message.")
public class Welcome extends Command {

    public Welcome(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        shell.writeln("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        shell.writeln("┃   Welcome to Quteshell   ┃");
        shell.writeln("┃ Use 'help' for commands. ┃");
        shell.writeln("┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }
}
