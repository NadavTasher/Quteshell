package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

@Command.Description("The history command lists the previously ran commands.")
public class History extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        for (int h = 0; h < shell.history().size(); h++) {
            shell.write(h + "\t");
            shell.writeln(shell.history().get(h));
        }
    }
}