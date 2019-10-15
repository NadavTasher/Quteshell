package quteshell;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

public class Console {
    /**
     * The destination writer.
     */
    private BufferedWriter writer = null;

    /**
     * The default constructor for the Console.
     */
    public Console() {
    }

    /**
     * The constructor for the Console.
     *
     * @param writer The destination writer
     */
    public Console(BufferedWriter writer) {
        this.writer = writer;
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
