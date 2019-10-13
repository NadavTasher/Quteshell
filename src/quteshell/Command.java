package quteshell;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Command {
    public void execute(Quteshell shell, String arguments) {

    }

    public String getName() {
        return getClass().getSimpleName().toLowerCase();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Description {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Anonymous{
    }
}