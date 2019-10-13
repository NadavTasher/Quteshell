package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

@Command.Description("The exit command closes the shell and disconnects.")
public class Exit extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.finish();
    }
}
