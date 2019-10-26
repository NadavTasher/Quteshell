/*
  Copyright (c) 2019 Nadav Tasher
  https://github.com/NadavTasher/Quteshell/
 */

package org.quteshell;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to select the command's minimum elevation.
 **/

@Retention(RetentionPolicy.RUNTIME)
public @interface Elevation {
    int DEFAULT = 1;
    int ALL = 0;
    int NONE = -1;

    int value();
}
