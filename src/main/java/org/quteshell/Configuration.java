package org.quteshell;

import org.quteshell.commands.Echo;
import org.quteshell.commands.Exit;
import org.quteshell.commands.Help;
import org.quteshell.commands.History;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class houses the configuration of the shell server
 */
public class Configuration {

    private boolean promptEnabled;
    private boolean logEnabled;
    private boolean ANSIEnabled;
    private String name;
    private OnConnect onConnect;
    private ArrayList<Class<? extends Command>> commands;

    public static final Configuration DEFAULT = new Configuration();

    /**
     * Default constructor.
     */
    public Configuration() {
        this.setName("qute");
        this.setPromptEnabled(true);
        this.setLogEnabled(false);
        this.setANSIEnabled(true);
        this.setOnConnect(shell -> {
            shell.writeln("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            shell.writeln("┃   Welcome to Quteshell   ┃");
            shell.writeln("┃ Use 'help' for commands. ┃");
            shell.writeln("┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        });
        this.setCommands(new ArrayList<>(Arrays.asList(Echo.class, Exit.class, Help.class, History.class)));
    }

    /**
     * Sets the prompt state.
     *
     * @param promptEnabled Prompt state
     */
    public void setPromptEnabled(boolean promptEnabled) {
        this.promptEnabled = promptEnabled;
    }

    /**
     * Returns the prompt state.
     *
     * @return Prompt state
     */
    public boolean isPromptEnabled() {
        return promptEnabled;
    }

    /**
     * Sets the log state.
     *
     * @param logEnabled Log state
     */
    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    /**
     * Returns the log state.
     *
     * @return Log state
     */
    public boolean isLogEnabled() {
        return logEnabled;
    }

    /**
     * Sets the ANSI state (colors).
     *
     * @param ANSIEnabled ANSI state
     */
    public void setANSIEnabled(boolean ANSIEnabled) {
        this.ANSIEnabled = ANSIEnabled;
    }

    /**
     * Returns the ANSI state (colors).
     *
     * @return ANSI state
     */
    public boolean isANSIEnabled() {
        return ANSIEnabled;
    }

    /**
     * Sets the name of the shell / service.
     *
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the shell / service.
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the connection callback.
     *
     * @param onConnect Connection callback
     */
    public void setOnConnect(OnConnect onConnect) {
        this.onConnect = onConnect;
    }

    /**
     * Returns the connection callback.
     *
     * @return Connection callback
     */
    public OnConnect getOnConnect() {
        return onConnect;
    }

    /**
     * Sets the commands array.
     *
     * @param commands Commands array
     */
    public void setCommands(ArrayList<Class<? extends Command>> commands) {
        this.commands = commands;
    }

    /**
     * Returns the commands array.
     *
     * @return Commands array
     */
    public ArrayList<Class<? extends Command>> getCommands() {
        return new ArrayList<>(commands);
    }

    /**
     * Adds a command to the commands array.
     *
     * @param command Command
     */
    public void addCommand(Class<? extends Command> command) {
        if (!commands.contains(command))
            commands.add(command);
    }

    /**
     * Removes a command from the commands array.
     * @param command Command
     */
    public void removeCommand(Class<? extends Command> command) {
        if (commands.contains(command))
            commands.remove(command);
    }

    /**
     * This interface is used as a connection callback.
     */
    public interface OnConnect {
        void onConnect(Shell shell);
    }
}
