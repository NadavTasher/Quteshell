package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

public class Exit extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.finish();
    }
}
