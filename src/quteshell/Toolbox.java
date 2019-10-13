package quteshell;

import java.util.Random;

public class Toolbox {

    private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String random(int length) {
        if (length > 0) {
            return CHARSET.charAt(new Random().nextInt(CHARSET.length())) + random(length - 1);
        }
        return "";
    }
}
