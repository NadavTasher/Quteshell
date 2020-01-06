/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is the main Shell element. It deals with console communication and command execution.
 */

public class Shell {

    private Configuration configuration;

    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private Thread thread = null;

    private boolean running;
    private int elevation;
    private String identifier;
    private ArrayList<Command> commands;
    private ArrayList<String> history;

    /**
     * Default constructor.
     *
     * @param socket Client-Server socket
     */
    public Shell(Socket socket, Configuration configuration) {
        if (socket != null &&
                configuration != null) {
            // Parameters
            this.socket = socket;
            this.configuration = configuration;
            // Defaults
            this.running = true;
            this.elevation = Elevation.DEFAULT;
            this.identifier = random(14);
            this.commands = new ArrayList<>();
            this.history = new ArrayList<>();
            // Setup Commands
            for (Class<? extends Command> command : configuration.getCommands()) {
                try {
                    this.commands.add(command.getDeclaredConstructor(Shell.class).newInstance(this));
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
                        // Run the connection callback
                        if (this.configuration.getOnConnect() != null) {
                            // Run callback
                            this.configuration.getOnConnect().onConnect(this);
                            // Draw prompt
                            if (this.configuration.isPromptEnabled())
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
    }

    /**
     * This function returns the Shell ID.
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
     * This function returns the command history.
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
        this.running = false;
    }

    /**
     * This function evaluates the input and executes the command.
     *
     * @param input Input from the socket
     */
    public void execute(String input) {
        print("Received '" + input + "'");
        if (input.length() > 0) {
            // Log input to history
            history.add(input);
            // Split input
            String[] split = input.split(" ", 2);
            for (Command command : this.commands) {
                if (this.elevation >= command.getClass().getAnnotation(Elevation.class).value()) {
                    if (command.getClass().getSimpleName().toLowerCase().equals(split[0])) {
                        // Execute the command
                        command.execute(split.length > 1 ? split[1] : null);
                        // Log execution
                        print("Command '" + split[0] + "' handled");
                        // Exit the function
                        return;
                    }
                }
            }
            // Write an error message to the socket
            writeln(configuration.getName() + ": " + split[0] + ": not handled");
            // Log execution
            print("Command '" + split[0] + "' not handled");
        }
    }

    /**
     * This function writes an output to the console.
     *
     * @param output Output
     */
    public void write(String output) {
        if (this.running) {
            if (this.writer != null) {
                try {
                    this.writer.write(output);
                    this.writer.flush();
                } catch (IOException ignored) {
                }
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
        write("\r\n");
    }

    /**
     * This function writes an output with a color and a newline to the console.
     *
     * @param output Output
     * @param color  Color
     */
    public void writeln(String output, Color color) {
        write(output, color);
        write("\r\n");
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
        if (this.configuration.isLogEnabled())
            System.out.println(identifier + " - " + text);
    }

    /**
     * This function takes an input and executes it, then prints a prompt if enabled.
     *
     * @param input Input
     */
    protected void input(String input) {
        execute(input);
        if (this.configuration.isPromptEnabled())
            prompt();
    }

    /**
     * This function displays a prompt on the console.
     */
    protected void prompt() {
        write(this.configuration.getName(), Color.LightGreen);
        write(":");
        write(String.valueOf(this.elevation), Color.LightBlue);
        write("> ");
    }

    /**
     * This function changes the color of the console.
     *
     * @param color Color
     */
    protected void color(Color color) {
        if (this.configuration.isANSIEnabled()) {
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
            write("\033[" + output + "m");
        }
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