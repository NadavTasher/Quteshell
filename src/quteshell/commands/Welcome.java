package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

@Command.Description("The welcome command displays a welcome message.")
public class Welcome extends Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("╔═══════════════════════════════════╗");
        shell.writeln("║       Welcome to Quteshell!       ║");
        shell.writeln("║ You can type 'help' for commands. ║");
        shell.writeln("╚═══════════════════════════════════╝");
    }
}
