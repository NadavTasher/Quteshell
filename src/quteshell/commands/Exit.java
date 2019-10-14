package quteshell.commands;

import quteshell.command.Command;
import quteshell.Quteshell;
import quteshell.command.Elevation;

@Elevation(Elevation.ALL)
@Help.Description("The exit command closes the shell and disconnects.")
public class Exit extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.finish();
    }
}
