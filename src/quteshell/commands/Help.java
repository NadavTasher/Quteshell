package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

public class Help extends Command {

    private static final int COLUMNS = 3;

    @Override
    public void execute(Quteshell shell, String arguments) {
        Command[] commands = shell.commands();
        shell.writeln("List of available commands:");
        for (int c = 0; c < commands.length; c += COLUMNS) {
            for (int r = 0; r < COLUMNS; r++) {
                if (c + r < commands.length) {
                    shell.write(commands[c + r].getName());
                }
                shell.write("\t\t");
            }
            shell.writeln();
        }
    }
}
