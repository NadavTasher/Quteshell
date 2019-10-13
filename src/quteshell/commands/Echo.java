package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

@Command.Description("The echo command prints back what you type in.")
public class Echo extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln(arguments);
    }
}
