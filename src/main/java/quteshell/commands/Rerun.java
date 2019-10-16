package quteshell.commands;

import quteshell.Quteshell;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.command.Exclude;

import java.util.ArrayList;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Elevation(Elevation.DEFAULT)
@Exclude
@Help.Description("The rerun command reruns the last command, or the given number of commands.")
public class Rerun implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
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
