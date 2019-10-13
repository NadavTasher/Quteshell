package quteshell;

public class Command {

    public void execute(Quteshell shell, String arguments) {

    }

    public String getName() {
        return getClass().getSimpleName().toLowerCase();
    }

    public boolean isStorable() {
        return true;
    }
}
