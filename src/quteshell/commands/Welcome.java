package quteshell.commands;

import quteshell.command.Command;
import quteshell.Quteshell;
import quteshell.command.Elevation;

@Elevation(Elevation.DEFAULT)
@Help.Description("The welcome command displays a welcome message.")
public class Welcome implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("╔═══════════════════════════════════╗");
        shell.writeln("║       Welcome to Quteshell!       ║");
        shell.writeln("║ You can type 'help' for commands. ║");
        shell.writeln("╚═══════════════════════════════════╝");
    }
}
