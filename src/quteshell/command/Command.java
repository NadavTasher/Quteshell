package quteshell.command;

import quteshell.Quteshell;

import java.lang.annotation.Annotation;

public interface Command {
    void execute(Quteshell shell, String arguments);
}