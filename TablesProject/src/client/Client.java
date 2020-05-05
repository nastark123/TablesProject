/*
    This file is the Client class, which is responsible for all communications from the client.  It provides convenience methods for all supported
    operations and serves as a gateway between the main client program and the server.
 */

package client;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import table.Table;
import table.TableEntry;


public class Client {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private volatile HashMap<String, Table> map;

    //basic constructor, ip is the server ip, port is the port (is this good documentation yet?)
    public Client(String ip, int port) throws IOException, ClassNotFoundException {
        socket = new Socket("localhost", 967);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        map = new HashMap<>();
        readMap();
    }

    //reads the HashMap from the server into the one on the client end, does NOT return the value, you must call getMap()
    public synchronized void readMap() throws IOException, ClassNotFoundException {
        out.write(1);

        int n = in.read();
        if(n == 1) {
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            HashMap<String, Table> map = (HashMap<String, Table>) objectInputStream.readObject();
            System.out.println(map.values());
            this.map = map;
        }
    }

    //adds the specified table to the server, does not refresh the client end
    public synchronized void addTable(Table t) throws IOException {
        out.write(3);

        int n = in.read();
        if(n == 3) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
//            objectOutputStream.writeObject(l);
            objectOutputStream.writeObject(t);
        }
    }

    //adds the specified entry to the specified table on the server
    public synchronized void addEntry(Table t, TableEntry e) throws IOException {
        out.write(4);

        int n = in.read();
        if(n == 4) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(t);
            objectOutputStream.writeObject(e);
        }
    }

    //updates the specified entry in the specified table on the server
    public synchronized void updateEntry(Table t, TableEntry e) throws IOException {
        out.write(6);

        int n = in.read();
        if(n == 6) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(t);
            objectOutputStream.writeObject(e);
        }
    }

    //removes the specified entry in the specified table on the server
    public synchronized void removeEntry(Table t, TableEntry e) throws IOException {
        out.write(5);

        int n = in.read();
        if(n == 5) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(t);
            objectOutputStream.writeObject(e);
        }
    }

    //pushes the local map to the server, and overwrites the one on the server end, DO NOT USE unless you absolutely know what you're doing, as the server
    //will not preserve the data, it will overwrite it
    public synchronized void writeMap(HashMap<String, Table> map) throws IOException {
        out.write(2);

        int n = in.read();
        if(n == 2) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(map);
        }
    }

    //returns the HashMap maintained on the client end
    public synchronized HashMap<String, Table> getMap() {
        return map;
    }

    //TODO refactor getMap() into getLocalMap and make another function that retrieves the master one

    //all of these functions deal with the local map, which can then be pushed to the server using the writeMap() function, I wouldn't recommend doing
    //it this way, but you do you
    public synchronized void updateMap(HashMap<String, Table> m) {
        map = m;
    }

    public synchronized void mapPut(Table t) {
        map.put(t.getLabel(), t);
    }

    public synchronized void mapRemove(Table t) {
        map.remove(t.getLabel());
    }

    public synchronized Table mapGet(Table t) {
        return map.get(t.getLabel());
    }

    public synchronized Table mapGet(String l) {
        return map.get(l);
    }
}
