/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell;

import org.quteshell.commands.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is the main Quteshell element. It deals with console communication and command execution.
 */

public class Quteshell {

    /**
     * This class manages the configuration for the shells.
     */
    public static class Configuration {

        /**
         * This class manages the commands for the shells.
         */
        public static class Commands {
            private static ArrayList<Class<? extends Command>> commands = new ArrayList<>();

            static {
                add(Clear.class);
                add(Echo.class);
                add(Exit.class);
                add(Help.class);
                add(History.class);
                add(Welcome.class);
            }

            /**
             * This function adds a command to the command array.
             *
             * @param command Command's Class
             */
            public static void add(Class<? extends Command> command) {
                Commands.commands.add(command);
            }

            /**
             * This function removes a command from the command array.
             *
             * @param command Command's Class
             */
            public static void remove(Class<? extends Command> command) {
                Commands.commands.remove(command);
            }

            /**
             * This function returns a copy of the command list.
             *
             * @return Command List
             */
            public static ArrayList<Class<? extends Command>> getCommands() {
                return new ArrayList<>(commands);
            }

            /**
             * This function returns the execution name of a given command.
             *
             * @param command Command
             * @return Name
             */
            public static String getName(Command command) {
                return command.getClass().getSimpleName().toLowerCase();
            }

            /**
             * This function returns the minimal elevation for a given command's execution.
             *
             * @param command Command
             * @return Minimal Elevation
             */
            public static int getElevation(Command command) {
                int minimal = Elevation.NONE;
                for (Annotation annotation : command.getClass().getAnnotations()) {
                    if (annotation instanceof Elevation) {
                        int elevation = ((Elevation) annotation).value();
                        if (minimal < 0 || minimal > elevation) {
                            minimal = elevation;
                        }
                    }
                }
                return minimal;
            }

        }

        /**
         * This interface is used to run an initialization command on a shell.
         */
        public interface OnConnect {
            void onConnect(Quteshell shell);
        }

        private static String name = "qute";
        private static boolean logState = false;
        private static boolean promptState = true;
        private static int IDLength = 14;
        private static int baseElevation = Elevation.DEFAULT;
        private static OnConnect onConnect = shell -> shell.execute("welcome");

        /**
         * This function returns the shell's name.
         *
         * @return Name
         */
        public static String getName() {
            return name;
        }

        /**
         * This function sets the shell's name.
         *
         * @param name Name
         */
        public static void setName(String name) {
            Configuration.name = name;
        }

        /**
         * This function returns the base elevation.
         *
         * @return Base Elevation
         */
        public static int getBaseElevation() {
            return baseElevation;
        }

        /**
         * This function sets the base elevation.
         *
         * @param baseElevation Base Elevation
         */
        public static void setBaseElevation(int baseElevation) {
            Configuration.baseElevation = baseElevation;
        }

        /**
         * This function returns the ID length.
         *
         * @return ID Length
         */
        public static int getIDLength() {
            return IDLength;
        }

        /**
         * This function sets the ID length.
         *
         * @param IDLength ID Length
         */
        public static void setIDLength(int IDLength) {
            Configuration.IDLength = IDLength;
        }

        /**
         * This function returns the logState state.
         *
         * @return Log State
         */
        public static boolean getLogState() {
            return logState;
        }

        /**
         * This function enables/disables logging.
         *
         * @param state State
         */
        public static void setLogState(boolean state) {
            Configuration.logState = state;
        }

        /**
         * This function returns the prompt state.
         *
         * @return Prompt State
         */
        public static boolean getPromptState() {
            return promptState;
        }

        /**
         * This function sets the prompt state.
         *
         * @param promptState Prompt State
         */
        public static void setPromptState(boolean promptState) {
            Configuration.promptState = promptState;
        }

        /**
         * This function returns the OnConnect interface which is ran on every shell when initialized.
         *
         * @return OnConnect
         */
        public static OnConnect getOnConnect() {
            return onConnect;
        }

        /**
         * This function sets the OnConnect interface which is ran on every shell when initialized.
         *
         * @param onConnect OnConnect
         */
        public static void setOnConnect(OnConnect onConnect) {
            Configuration.onConnect = onConnect;
        }
    }

    // Socket & I/O
    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private Thread thread = null;

    // Shell
    private boolean running = false;
    private int elevation = Elevation.NONE;
    private String prompt = null;
    private String identifier = null;
    private ArrayList<Command> commands = null;
    private ArrayList<String> history = null;

    /**
     * Default constructor.
     *
     * @param socket Client-Server socket
     */
    public Quteshell(Socket socket) {
        this.socket = socket;
        this.running = true;
        this.elevation = Configuration.getBaseElevation();
        this.prompt = Configuration.getName() != null ? Configuration.getName() : "@";
        this.identifier = random(Configuration.getIDLength());
        this.commands = new ArrayList<>();
        this.history = new ArrayList<>();
        // Setup Commands
        for (Class<? extends Command> command : Configuration.Commands.getCommands()) {
            try {
                this.commands.add(command.newInstance());
            } catch (Exception ignored) {
            }
        }
        // Setup I/O
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            print("I/O Setup Completed");
        } catch (IOException e) {
            reader = null;
            writer = null;
            print("I/O Setup Failed");
        } finally {
            if (reader != null && writer != null) {
                thread = new Thread(() -> {
                    // Run the OnConnect
                    if (Configuration.getOnConnect() != null) {
                        // Run callback
                        Configuration.getOnConnect().onConnect(this);
                        // Draw prompt
                        if (Configuration.getPromptState())
                            prompt();
                    }
                    // Wrap command listener with a try/catch
                    try {
                        // Begin listening
                        while (running) {
                            try {
                                if (reader.ready()) {
                                    input(reader.readLine());
                                }
                                Thread.sleep(10);
                            } catch (IOException e) {
                                print("Failed to read input stream.");
                            } catch (InterruptedException e) {
                                print("Failed to sleep.");
                            }
                        }
                    } catch (Exception e) {
                        print("Unrecoverable exception: " + e.toString());
                    }
                    // Finish listening
                    print("Finished");
                    try {
                        this.socket.close();
                    } catch (Exception e) {
                        print("Failed to close socket.");
                    }
                });
                thread.start();
            }
        }
    }

    /**
     * This function returns the Quteshell ID.
     *
     * @return ID
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * This function returns the shell's elevation.
     *
     * @return Elevation
     */
    public int getElevation() {
        return elevation;
    }

    /**
     * This function sets the shell's elevation.
     *
     * @param elevation Elevation
     */
    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    /**
     * This function returns whether the shell is running.
     *
     * @return Running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * This function returns the command list for the current elevation.
     *
     * @return Commands
     */
    public ArrayList<Command> getCommands() {
        // Categorize commands by elevation
        ArrayList<Command> commands = new ArrayList<>();
        for (Command command : this.commands) {
            int elevation = Configuration.Commands.getElevation(command);
            if (elevation != Elevation.NONE) {
                if (elevation == Elevation.ALL || this.elevation >= elevation) {
                    commands.add(command);
                }
            }
        }
        return commands;
    }

    /**
     * This function returns the command getHistory.
     *
     * @return History
     */
    public ArrayList<String> getHistory() {
        return new ArrayList<>(history);
    }

    /**
     * This function stops the shell.
     */
    public void finish() {
        running = false;
    }

    /**
     * This function evaluates the input and executes the command.
     *
     * @param input Input from the socket
     */
    public void execute(String input) {
        print("Evaluating '" + input + "'");
        if (input.length() > 0) {
            String[] split = input.split(" ", 2);
            Command run = null;
            for (Command command : getCommands()) {
                if (Configuration.Commands.getName(command).equals(split[0])) {
                    run = command;
                    break;
                }
            }
            if (run != null) {
                history.add(input);
                // Execute the command
                run.execute(this, split.length > 1 ? split[1] : null);
                print("Command '" + split[0] + "' handled");
            } else {
                // Write an error message to the socket
                writeln(prompt + ": " + split[0] + ": not handled");
                print("Command '" + split[0] + "' not handled");
            }
        }
    }

    /**
     * This function clears the whole console and returns to top.
     */
    public void clearAll() {
        control("2J");
        control("H");
    }

    /**
     * This function clears the whole line and returns to the beginning.
     */
    public void clearLine() {
        control("2K");
        write("\r");
    }

    /**
     * This function writes an output to the console.
     *
     * @param output Output
     */
    public void write(String output) {
        if (writer != null) {
            try {
                writer.write(output);
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * This function writes an output with color to the console.
     *
     * @param output Output
     * @param color  Color
     */
    public void write(String output, Color color) {
        color(color);
        write(output);
        color(Color.None);
    }

    /**
     * This function writes an output with a newline to the console.
     *
     * @param output Ouput
     */
    public void writeln(String output) {
        write(output);
        write("\n");
    }

    /**
     * This function writes an output with a color and a newline to the console.
     *
     * @param output Output
     * @param color  Color
     */
    public void writeln(String output, Color color) {
        write(output, color);
        write("\n");
    }

    /**
     * This function prints a newline to the console.
     */
    public void writeln() {
        writeln("");
    }

    /**
     * This function prints to the host's console.
     *
     * @param text Text to print
     */
    protected void print(String text) {
        if (Configuration.getLogState())
            System.out.println(identifier + " - " + text);
    }

    /**
     * This function takes an input and executes it, then prints a prompt if enabled.
     *
     * @param input Input
     */
    protected void input(String input) {
        execute(input);
        if (Configuration.getPromptState())
            prompt();
    }

    /**
     * This function displays a prompt on the console.
     */
    protected void prompt() {
        write(this.prompt, Color.LightGreen);
        write(":");
        write(String.valueOf(this.elevation), Color.LightBlue);
        write("> ");
    }

    /**
     * This funtion writes a control sequence to the console.
     *
     * @param sequence Control Sequence
     */
    protected void control(String sequence) {
        write("\033[" + sequence);
    }

    /**
     * This function changes the color of the console.
     *
     * @param color Color
     */
    protected void color(Color color) {
        String output;
        switch (color) {
            case Black:
                output = "0;30";
                break;
            case DarkGray:
                output = "1;30";
                break;
            case Red:
                output = "0;31";
                break;
            case LightRed:
                output = "1;31";
                break;
            case Green:
                output = "0;32";
                break;
            case LightGreen:
                output = "1;32";
                break;
            case Orange:
                output = "0;33";
                break;
            case LightOrange:
                output = "1;33";
                break;
            case Blue:
                output = "0;34";
                break;
            case LightBlue:
                output = "1;34";
                break;
            case Purple:
                output = "0;35";
                break;
            case LightPurple:
                output = "1;35";
                break;
            case Cyan:
                output = "0;36";
                break;
            case LightCyan:
                output = "1;36";
                break;
            case LightGray:
                output = "0;37";
                break;
            case White:
                output = "1;37";
                break;
            case None:
            default:
                output = "0";
        }
        control(output + "m");
    }

    /**
     * This function generates random strings with a specific length.
     *
     * @param length Length of string
     * @return Random string
     */
    protected static String random(int length) {
        final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
        if (length > 0) {
            return charset.charAt(new Random().nextInt(charset.length())) + random(length - 1);
        }
        return "";
    }

    /**
     * This enum is to define console colors.
     */
    public enum Color {
        None,
        Black,
        DarkGray,
        Red,
        LightRed,
        Green,
        LightGreen,
        Orange,
        LightOrange,
        Blue,
        LightBlue,
        Purple,
        LightPurple,
        Cyan,
        LightCyan,
        LightGray,
        White,
    }
}