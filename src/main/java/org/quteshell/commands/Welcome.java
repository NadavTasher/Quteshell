/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell.commands;

import org.quteshell.Elevation;
import org.quteshell.Command;
import org.quteshell.Quteshell;

@Elevation(Elevation.DEFAULT)
@Help.Description("The welcome command displays a welcome message.")
public class Welcome implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        shell.writeln("┃   Welcome to Quteshell   ┃");
        shell.writeln("┃ Use 'help' for commands. ┃");
        shell.writeln("┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }
}
