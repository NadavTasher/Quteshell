import quteshell.Quteshell;

import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

public class Main {

    private static final int PORT = 7000;
    private static final ArrayList<Quteshell> quteshells = new ArrayList<>();

    private static boolean listening = true;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (listening) {
                quteshells.add(new Quteshell(serverSocket.accept()).begin());
            }
        } catch (Exception e) {
            System.out.println("Host - " + e.getMessage());
        }
    }
}
