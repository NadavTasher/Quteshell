package quteshell;

import java.io.*;
import java.net.Socket;

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
                    if (reader.ready())
                        read(reader.readLine());
                } catch (IOException e) {
                    print("Failed to read input stream.");
                }
            });
            thread.start();
        }
        return this;
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
     * This function evaluates the input and executes the command.
     *
     * @param input Input from the socket
     */
    private void execute(String input) {

    }

    /**
     * This function prints the prompt to the socket (qute:$>).
     */
    private void prompt() {
        write(name);
        write(elevated ? PROMPT_ELEVATED : PROMPT_UNELEVATED);
        write(" ");
    }

    /**
     * This function clears the client's terminal.
     */
    private void clear() {
        write("\033[2J\033[H");
    }

    /**
     * This function writes an output to the socket, with a newline.
     *
     * @param output Output
     */
    private void writeln(String output) {
        write(output + "\n");
    }

    /**
     * This function writes an output to the socket.
     *
     * @param output Output
     */
    private void write(String output) {
        try {
            writer.write(output);
            writer.flush();
        } catch (IOException ignored) {
            print("Failed to write output stream.");
        }
    }

}