package quteshell.command;

import quteshell.Quteshell;

import java.lang.annotation.Annotation;

public class Command {
    public void execute(Quteshell shell, String arguments) {

    }

    public String getName() {
        return getClass().getSimpleName().toLowerCase();
    }

    public int getElevation() {
        int minimal = Elevation.NONE;
        for (Annotation annotation : getClass().getAnnotations()) {
            if (annotation instanceof Elevation) {
                int elevation = ((Elevation) annotation).value();
                if (minimal < 0) {
                    minimal = elevation;
                } else {
                    if (minimal > elevation) {
                        minimal = elevation;
                    }
                }
            }
        }
        return minimal;
    }
}