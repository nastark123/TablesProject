/*
    This file is the TableServer class, which serves to create the threads that the clients interact with, as well as maintains the master HashMap
    used to store all of the Table objects that the user requests.  This class should be threadsafe, but it has not been tested thoroughly enough
    for me to say that for sure.
*/

package tablesproject.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import tablesproject.table.Table;

public class TableServer {
    private Socket socket;
    private ServerSocket sSocket;
    private InputStream in;
    private OutputStream out;
    private volatile HashMap<String, Table> map;

    //Constructor, not much to look at here
    public TableServer(int port) throws IOException {
        sSocket = new ServerSocket(port);
        map = new HashMap<>();
    }

    //this function WILL block, so don't run this in your main thread
    public void waitForConn() throws IOException {
        while(true) {
            socket = sSocket.accept();
            in = socket.getInputStream();
            out = socket.getOutputStream();
            TableServerThread thread = new TableServerThread(socket, in, out, this);
            thread.start();
        }
    }

    //returns the HashMap that is maintained by this tablesproject.server object
    public synchronized HashMap<String, Table> getMap() {
        return map;
    }

    //updates the map to the argument passed in, I would not recommend using unless you are sure you know what you're doing as this can cause issues
    public synchronized void updateMap(HashMap<String, Table> m) {
        map = m;
    }

    //adds the specified Table to the map
    public synchronized void mapPut(Table t) {
        map.put(t.getLabel(), t);
    }

    //removes the specified Table from the map
    public synchronized void mapRemove(Table t) {
        map.remove(t.getLabel());
    }

    //removes the Table with the specified label from the map
    public synchronized void mapRemove(String l) {
        map.remove(l);
    }

    //returns the specified Table
    public synchronized Table mapGet(Table t) {
        return map.get(t.getLabel());
    }

    //returns the Table with the specified label from the map
    public synchronized Table mapGet(String l) {
        return map.get(l);
    }

}
