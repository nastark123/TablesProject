/*
    This file is the TableClient class, which is responsible for all communications from the tablesproject.client.  It provides convenience methods for all supported
    operations and serves as a gateway between the main tablesproject.client program and the tablesproject.server.
 */

package tablesproject.client;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import tablesproject.table.Table;
import tablesproject.table.TableEntry;

//TODO make the interfacing with the map more abstracted and not clunky

public class TableClient {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private volatile HashMap<String, Table> map;
    private boolean autoUpdate = false;

    //basic constructor, ip is the server ip, port is the port (is this good documentation yet?)
    public TableClient(String ip, int port) throws IOException, ClassNotFoundException {
        socket = new Socket(ip, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        map = new HashMap<>();
        readMap();
    }

    //constructor that sets the autoUpdate boolean
    public TableClient(String ip, int port, boolean autoUpdate) throws IOException,ClassNotFoundException {
        socket = new Socket(ip, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        map = new HashMap<>();
        this.autoUpdate = true;
        readMap();
    }

    //disconnects from the server
    public synchronized void disconnect() throws IOException {
        out.write(0);

        int n = in.read();
        if(n == 0) {
            socket.close();
        }
    }

    //reads the HashMap from the server into the one on the client end, does NOT return the value, you must call getMap()
    public synchronized void readMap() throws IOException, ClassNotFoundException {
        out.write(1);

        int n = in.read();
        if(n == 1) {
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            HashMap<String, Table> map = (HashMap<String, Table>) objectInputStream.readObject();
//            System.out.println(map.values());
            this.map = map;
        }
    }

    //adds the specified table to the server, does not refresh the client end unless autoupdate is specified
    public synchronized void addTable(Table t) throws IOException {
        out.write(3);

        int n = in.read();
        if(n == 3) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(t);
            if(autoUpdate) {
                try {
                    readMap();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
            if(autoUpdate) {
                try {
                    readMap();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
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
            if(autoUpdate) {
                try {
                    readMap();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
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
            if(autoUpdate) {
                try {
                    readMap();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
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
    public synchronized HashMap<String, Table> getLocalMap() {
        return map;
    }

    //updates and returns the map from the server
    public synchronized HashMap<String, Table> getMap() throws IOException, ClassNotFoundException {
        readMap();
        return map;
    }

    //all of these functions deal with the local map, which can then be pushed to the server using the writeMap() function, I wouldn't recommend doing
    //it this way unless you set autoUpdate to true
    public synchronized void updateMap(HashMap<String, Table> m) {
        map = m;
    }

    public synchronized void mapPut(Table t) {
        map.put(t.getLabel(), t);
    }

    public synchronized void mapRemove(Table t) {
        map.remove(t.getLabel());
    }

    //these just get tables from the locally stored one, which if autoUpdate is true will be the last time that any push function was called
    public synchronized Table getTable(Table t) {
        return map.get(t.getLabel());
    }

    public synchronized Table getTable(String l) {
        if(!l.contains("/")) return map.get(l);
        String[] path = l.split("/");
        Table t = map.get(path[0]);
        Table temp;
        for(int i = 1; i < path.length; i++) {
            temp = t;
            t = temp.getEntry(path[i]).getSubTable();
            if(t == null) return null;
        }
        return t;
    }
}
