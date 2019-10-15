package quteshell.command;

import quteshell.Quteshell;

import java.lang.annotation.Annotation;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

public interface Command {
    void execute(Quteshell shell, String arguments);
}