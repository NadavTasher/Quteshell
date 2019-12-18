/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell;

/**
 * This class is used to create commands.
 */
@Elevation(Elevation.DEFAULT)
public class Command {

    /**
     * Parent shell
     */
    protected Shell shell;

    /**
     * This is the default constructor for a command.
     *
     * @param shell Parent shell
     */
    public Command(Shell shell) {
        this.shell = shell;
    }

    /**
     * This function should get overridden, handles command execution.
     *
     * @param arguments Command arguments
     */
    public void execute(String arguments) {

    }
}