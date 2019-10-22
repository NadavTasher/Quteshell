package org.quteshell.command;

import org.quteshell.Quteshell;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

public interface Command {
    void execute(Quteshell shell, String arguments);
}