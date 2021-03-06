/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell;

import java.lang.annotation.Annotation;

/**
 * This class is used to create commands.
 */
@Elevation
public class Command {

    /**
     * Parent shell
     */
    protected Shell shell;

    /**
     * Default constructor.
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