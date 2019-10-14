package quteshell.command;

import java.lang.annotation.Annotation;

public class Toolbox {
    public static String getName(Command command) {
        return command.getClass().getSimpleName().toLowerCase();
    }

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

    public static boolean isIncludable(Command command) {
        for (Annotation annotation : command.getClass().getAnnotations()) {
            if (annotation instanceof Exclude) {
                return false;
            }
        }
        return true;
    }
}
