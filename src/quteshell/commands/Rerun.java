package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

import java.util.ArrayList;

@Command.Anonymous
@Command.Description("The rerun command reruns the last command, or the given number of commands.")
public class Rerun extends Command {
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
        ArrayList<String> history = shell.history();
        int start = history.size() - rerun;
        int end = history.size();
        for (int c = ((start < 0) ? 0 : start); c < end; c++) {
            shell.execute(history.get(c));
        }
    }
}
