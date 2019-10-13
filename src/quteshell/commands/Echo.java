package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

public class Echo extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln(arguments);
    }
}
