/*
    This file is the ServerThread class, which handles the interactions with clients.  Each client will have its own thread, and each thread is
    responsible for interacting with the main HashMap in the Server class that it belongs to.
    TODO change the command codes from standard ints to an enum
 */

package server;

import table.Table;
import table.TableEntry;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    private Socket s;
    private InputStream in;
    private OutputStream out;
    private Server server;

    public ServerThread(Socket s, InputStream in, OutputStream out, Server server) {
        this.s = s;
        this.in = in;
        this.out = out;
        this.server = server;
        System.out.println("New client connected: " + s.getInetAddress());
    }

    @Override
    public void run() {
        while(true) {
            try {
                int n = in.read();
                System.out.println(n);
                switch (n) {
                    case 1:
                        writeMap(server.getMap());
                        break;
                    case 2:
                        server.updateMap(readMap());
                        break;
                    case 3:
                        addTable();
                        break;
                    case 4:
                        addEntry();
                        break;
                    case 5:
                        removeEntry();
                        break;
                    case 6:
                        updateEntry();
                        break;
                }
                System.out.println(server.getMap().values());
            } catch (Exception e) {

            }
        }
    }

    public HashMap<String, Table> readMap() throws IOException, ClassNotFoundException {
        out.write(2);
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        HashMap<String, Table> map = (HashMap<String, Table>) objectInputStream.readObject();
        System.out.println(map.values());
        return map;
    }

    public void writeMap(HashMap<String, Table> map) throws IOException {
        out.write(1);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(map);

    }

    public void addEntry() throws IOException, ClassNotFoundException {
        out.write(4);

        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Table t = (Table) objectInputStream.readObject();
        String label = t.getLabel();
        TableEntry e = (TableEntry) objectInputStream.readObject();
        t = server.mapGet(label);
        t.addEntry(e);
        server.mapRemove(t);
        server.mapPut(t);
    }

    public void removeEntry() throws IOException, ClassNotFoundException {
        out.write(5);

        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Table t = (Table) objectInputStream.readObject();
        String label = t.getLabel();
        TableEntry e = (TableEntry) objectInputStream.readObject();
        t = server.mapGet(label);
        t.removeEntry(e);
        server.mapRemove(t);
        server.mapPut(t);
    }

    public void updateEntry() throws IOException, ClassNotFoundException {
        out.write(6);

        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Table t = (Table) objectInputStream.readObject();
        String label = t.getLabel();
        TableEntry e = (TableEntry) objectInputStream.readObject();
        t = server.mapGet(label);
        t.update(e);
        server.mapRemove(t);
        server.mapPut(t);
    }

    public void addTable() throws IOException, ClassNotFoundException {
        out.write(3);

        ObjectInputStream objectInputStream = new ObjectInputStream((in));
//        String l = (String) objectInputStream.readObject();
        Table t = (Table) objectInputStream.readObject();
        server.mapPut(t);
    }
}
