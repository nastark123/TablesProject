package frc.robot;

import java.io.IOException;

import tablesproject.server.TableServer;

public class ServerRunner extends Thread {
    private TableServer server;

    public ServerRunner(TableServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.waitForConn();
        } catch (IOException e) {
            //handle any errors here and do what you want with them
            e.printStackTrace();
        }
    }
}