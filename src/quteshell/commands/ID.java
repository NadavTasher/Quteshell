package quteshell.commands;

import quteshell.command.Command;
import quteshell.Quteshell;
import quteshell.command.Elevation;

@Elevation(Elevation.DEFAULT)
@Help.Description("The id command prints the ID of the shell.")
public class ID implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln(shell.getID());
    }
}
