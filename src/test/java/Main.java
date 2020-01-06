import commands.TestCommand;
import org.quteshell.Configuration;
import org.quteshell.Shell;

import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Copyright (c) 2019 Nadav Tasher
 * https://github.com/NadavTasher/Quteshell/
 **/

public class Main {

    private static final int PORT = 7000;
    private static final ArrayList<Shell> shells = new ArrayList<>();

    private static boolean listening = true;

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setLogEnabled(true);
        configuration.setANSIEnabled(true);
        configuration.addCommand(TestCommand.class);
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (listening) {
                shells.add(new Shell(serverSocket.accept(), configuration));
            }
        } catch (Exception e) {
            System.out.println("Host - " + e.getMessage());
        }
    }
}
