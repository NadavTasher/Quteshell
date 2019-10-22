package org.quteshell.commands;

import org.quteshell.command.Elevation;
import org.quteshell.Quteshell;
import org.quteshell.command.Command;

import java.util.ArrayList;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Elevation(Elevation.DEFAULT)
@Help.Description("The rerun command reruns the last command, or the given number of commands.")
public class Rerun implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.getHistory().remove(shell.getHistory().size() - 1);
        int rerun = 1;
        if (arguments != null) {
            try {
                rerun = Integer.parseInt(arguments);
            } catch (Exception ignored) {
                shell.writeln("Not a number!");
            }
        }
        ArrayList<String> history = shell.getHistory();
        int start = history.size() - rerun;
        int end = history.size();
        for (int c = ((start < 0) ? 0 : start); c < end; c++) {
            shell.execute(history.get(c));
        }
    }
}
