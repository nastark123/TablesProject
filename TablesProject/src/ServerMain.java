/*
    This file is a sample main program to run a server, yours will probably be significantly more advanced.
 */

import server.Server;
import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new Server(967);
        server.waitForConn();
    }
}
