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

public class Quteshell extends Console {

    // Constants
    private static final String NAME = "qute";

    // ID & Host access
    private String id = random(14);

    // Socket & I/O
    private Socket socket;
    private BufferedReader reader;
    private Thread thread = null;

    // Shell
    private boolean running = true;
    private int elevation = Elevation.DEFAULT;
    private final ArrayList<Command> commands = new ArrayList<>();

    // History
    private ArrayList<String> history = new ArrayList<>();

    // UI
    private String prompt = NAME;

    /**
     * Default constructor without a prompt.
     *
     * @param socket Client-Server socket
     */
    public Quteshell(Socket socket) {
        this.socket = socket;
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
        if (thread == null) {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                setWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            } catch (IOException e) {
                reader = null;
                setWriter(null);
                print("Failed to initialize I/O streams.");
            }
            if (reader != null) {
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
                        socket.close();
                    } catch (Exception e) {
                        print("Failed to close socket.");
                    }
                });
                thread.start();
            }
        }
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

}