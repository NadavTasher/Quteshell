package quteshell;

import quteshell.commands.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.Socket;
import java.util.ArrayList;

public class Quteshell {

    // Constants
    private static final String PROMPT_UNELEVATED = ":$>";
    private static final String PROMPT_ELEVATED = ":#>";

    // ID & Host access
    private String id = Toolbox.random(10);

    // Socket & I/O
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Thread thread = null;

    // Shell
    private boolean running = true;
    private boolean elevated = false;

    // Shell commands
    private final Command[] COMMANDS_ELEVATED = {
    };
    private final Command[] COMMANDS_UNELEVATED = {
            new Welcome(),
            new Help(),
            new Clear(),
            new Echo(),
            new History(),
            new Rerun(),
            new Exit()
    };

    // History
    private ArrayList<String> history = new ArrayList<>();

    // UI
    private String name = "qute";

    /**
     * Default constructor without a name.
     *
     * @param socket Client-Server socket
     */
    public Quteshell(Socket socket) {
        this.socket = socket;
    }

    /**
     * Custom constructor with a name.
     *
     * @param socket Client-Server socket
     * @param name   Shell name
     */
    public Quteshell(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
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
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                reader = null;
                writer = null;
                print("Failed to initialize I/O streams.");
            }
            if (reader != null && writer != null) {
                thread = new Thread(() -> {
                    try {
                        // Initialize a welcome message
                        read("welcome");
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
                        // Finish listening
                        print("Finished");
                        socket.close();
                    } catch (Exception e) {
                        print("Failed to close socket.");
                    }
                    thread.stop();
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
        prompt();
    }

    /**
     * This function returns the command list for the current context.
     *
     * @return Commands
     */
    public Command[] commands() {
        if (elevated)
            return COMMANDS_ELEVATED;
        else
            return COMMANDS_UNELEVATED;
    }

    /**
     * This function returns the command history.
     *
     * @return History
     */
    public ArrayList<String> history() {
        return history;
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
            for (Command command : commands()) {
                if (command.getName().equals(split[0])) {
                    run = command;
                    break;
                }
            }
            if (run != null) {
                // Check if command is storable and store it in history
                boolean store = true;
                for (Annotation annotation : run.getClass().getAnnotations()) {
                    if (annotation instanceof Command.Anonymous) {
                        store = false;
                    }
                }
                if (store)
                    history.add(input);
                // Execute the command
                run.execute(this, split.length > 1 ? split[1] : null);
                print("Command '" + split[0] + "' handled.");
            } else {
                // Write an error message to the socket
                writeln(name + ": " + split[0] + ": not handled");
                print("Command '" + split[0] + "' not handled.");
            }
        }
    }

    /**
     * This function prints the prompt to the socket (qute:$>).
     */
    public void prompt() {
        write(name);
        write(elevated ? PROMPT_ELEVATED : PROMPT_UNELEVATED);
        write(" ");
    }

    /**
     * This function clears the client's terminal.
     */
    public void clear() {
        write("\033[2J\033[H");
    }

    /**
     * This function writes a newline to the socket.
     */
    public void writeln() {
        writeln("");
    }

    /**
     * This function writes an output to the socket, with a newline.
     *
     * @param output Output
     */
    public void writeln(String output) {
        write(output + "\n");
    }

    /**
     * This function writes an output to the socket.
     *
     * @param output Output
     */
    public void write(String output) {
        try {
            writer.write(output);
            writer.flush();
        } catch (IOException ignored) {
            print("Failed to write output stream.");
        }
    }

}