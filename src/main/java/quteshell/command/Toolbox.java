package quteshell.command;

import java.lang.annotation.Annotation;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

public class Toolbox {

    /**
     * This function returns the execution name of a given command.
     * @param command Command
     * @return Name
     */
    public static String getName(Command command) {
        return command.getClass().getSimpleName().toLowerCase();
    }

    /**
     * This function returns the minimal elevation for a given command's execution.
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

    /**
     * This function returns the includability of a given command in the execution history.
     * @param command Command
     * @return Includability
     */
    public static boolean isIncludable(Command command) {
        for (Annotation annotation : command.getClass().getAnnotations()) {
            if (annotation instanceof Exclude) {
                return false;
            }
        }
        return true;
    }
}
