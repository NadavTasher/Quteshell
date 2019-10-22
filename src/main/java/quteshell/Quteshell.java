package quteshell;

import org.reflections.Reflections;
import quteshell.command.Command;
import quteshell.command.Elevation;
import quteshell.command.Toolbox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

public class Quteshell {

    public static class Configuration {
        private static String name = "qute";
        private static boolean logState = false;
        private static int IDLength = 14;
        private static int baseElevation = Elevation.DEFAULT;

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
         * @return Log State
         */
        public static boolean getLogState() {
            return logState;
        }

        /**
         * This function enables/disables logging.
         * @param state State
         */
        public static void setLogState(boolean state) {
            Configuration.logState = state;
        }
    }

    // ID & Host access
    private String id = random(Configuration.getIDLength());

    // Socket & I/O
    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private Thread thread = null;

    // Shell
    private boolean running = true;
    private int elevation = Configuration.getBaseElevation();
    private final ArrayList<Command> commands = new ArrayList<>();

    // History
    private ArrayList<String> history = new ArrayList<>();

    // UI
    private String prompt = Configuration.getName();

    /**
     * Default constructor without a prompt.
     *
     * @param socket Client-Server socket
     */
    public Quteshell(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            print("I/O Setup Completed");
        } catch (IOException e) {
            reader = null;
            writer = null;
            print("I/O Setup Failed");
        } finally {
            thread = new Thread(() -> {
                // Initialize a welcome message
                read("welcome");
                try {
                    // Begin listening
                    while (running) {
                        try {
                            if (reader.ready())
                                read(reader.readLine());
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

    /**
     * This function returns the Quteshell ID.
     *
     * @return ID
     */
    public String getID() {
        return id;
    }

    /**
     * This function returns the command list for the current elevation.
     *
     * @return Commands
     */
    public ArrayList<Command> getCommands() {
        // Initialize commands array
        if (this.commands.isEmpty()) {
            for (Class<? extends Command> command : new Reflections(getClass()).getSubTypesOf(Command.class)) {
                try {
                    this.commands.add(command.newInstance());
                } catch (Exception ignored) {
                }
            }
        }
        // Categorize commands by elevation
        ArrayList<Command> commands = new ArrayList<>();
        for (Command command : this.commands) {
            int elevation = Toolbox.getElevation(command);
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
        return history;
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
     * This function begins the shell's communication with the client.
     *
     * @return Quteshell instance
     */
    public Quteshell begin() {

        return this;
    }

    /**
     * This function stops the shell.
     */
    public void finish() {
        running = false;
    }

    /**
     * This function prints to the host's console.
     *
     * @param text Text to print
     */
    private void print(String text) {
        System.out.println(id + " - " + text);
    }

    /**
     * This function if called when a new input is entered.
     *
     * @param input Input from the socket
     */
    private void read(String input) {
        execute(input);
        prompt(prompt, elevation);
    }

    /**
     * This function generates random strings with a specific length.
     *
     * @param length Length of string
     * @return Random string
     */
    private String random(int length) {
        final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
        if (length > 0) {
            return charset.charAt(new Random().nextInt(charset.length())) + random(length - 1);
        }
        return "";
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
                if (Toolbox.getName(command).equals(split[0])) {
                    run = command;
                    break;
                }
            }
            if (run != null) {
                // Check if command is storable and store it in getHistory
                if (Toolbox.isIncludable(run))
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
    public void clear() {
        control("2J");
        control("H");
    }

    /**
     * This function clears the whole line and returns to the beginning.
     */
    public void clearline() {
        control("2K");
        write("\r");
    }

    /**
     * This function displays a prompt on the console.
     *
     * @param name      Name of shell
     * @param elevation Elevation of shell
     */
    public void prompt(String name, int elevation) {
        write(name, Color.LightGreen);
        write(":");
        write(String.valueOf(elevation), Color.LightBlue);
        write("> ");
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
     * @param output
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
     * This funtion writes a control sequence to the console.
     *
     * @param sequence Control Sequence
     */
    private void control(String sequence) {
        write("\033[" + sequence);
    }

    /**
     * This function changes the color of the console.
     *
     * @param color Color
     */
    private void color(Color color) {
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
     * Setter for the writer
     *
     * @param writer Writer
     */
    protected void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    /**
     * Getter for the writer
     *
     * @return Writer
     */
    protected BufferedWriter getWriter() {
        return this.writer;
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