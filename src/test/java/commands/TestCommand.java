package commands;

import org.quteshell.Shell;
import org.quteshell.Command;
import org.quteshell.commands.Help;

@Help.Description("This command is a test command")
public class TestCommand extends Command {

    public TestCommand(Shell shell) {
        super(shell);
    }

    @Override
    public void execute(String arguments) {
        shell.writeln("This is a test command.");
    }
}
