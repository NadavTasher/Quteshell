package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

@Command.Description("The clear command clears the client's terminal.")
public class Clear extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.clear();
    }
}
