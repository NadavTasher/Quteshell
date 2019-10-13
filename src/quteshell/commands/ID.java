package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

@Command.Description("The id command prints the ID of the shell.")
public class ID extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln(shell.id());
    }
}
