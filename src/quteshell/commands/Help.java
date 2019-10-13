package quteshell.commands;

import quteshell.Command;
import quteshell.Quteshell;

import java.lang.annotation.Annotation;

@Command.Description("The help command lists all commands, or a command description.")
public class Help extends Command {

    private static final int COLUMNS = 3;

    @Override
    public void execute(Quteshell shell, String arguments) {
        Command[] commands = shell.commands();
        if (arguments == null) {
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
        } else {
            shell.writeln(getName() + " - '" + arguments + "'");
            Command help = null;
            for (Command command : commands) {
                if (command.getName().equals(arguments)) {
                    help = command;
                    break;
                }
            }
            String text;
            if (help != null) {
                text = "No description available";
                for (Annotation annotation : help.getClass().getAnnotations()) {
                    if (annotation instanceof Description) {
                        text = ((Description) annotation).value();
                    }
                }
            } else {
                text = "Command not found";
            }
            shell.writeln(text);
        }
    }
}
