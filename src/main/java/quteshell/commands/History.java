package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Elevation(Elevation.DEFAULT)
@Help.Description("The history command lists the previously ran commands.")
public class History implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        for (int h = 0; h < shell.getHistory().size(); h++) {
            shell.write(h + "\t");
            shell.writeln(shell.getHistory().get(h));
        }
    }
}