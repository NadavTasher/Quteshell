package commands;

import org.quteshell.Quteshell;
import org.quteshell.Command;
import org.quteshell.Elevation;
import org.quteshell.commands.Help;

@Elevation(Elevation.DEFAULT)
@Help.Description("This command is a test command")
public class TestCommand implements Command {
    @Override
    public void execute(Quteshell shell, String arguments) {
        shell.writeln("This is a test command.");
    }
}
