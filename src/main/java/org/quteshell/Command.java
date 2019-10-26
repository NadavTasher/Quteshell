/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell;

/**
 * This interface is used to create command.
 */

public interface Command {
    void execute(Quteshell shell, String arguments);
}