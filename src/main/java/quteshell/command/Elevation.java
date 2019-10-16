package quteshell.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

@Retention(RetentionPolicy.RUNTIME)
public @interface Elevation {
    int DEFAULT = 1;
    int ALL = 0;
    int NONE = -1;

    int value();
}
