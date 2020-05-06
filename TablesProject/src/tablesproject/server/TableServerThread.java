/*
    This file is the TableServerThread class, which handles the interactions with clients.  Each tablesproject.client will have its own thread, and each thread is
    responsible for interacting with the main HashMap in the TableServer class that it belongs to.
    TODO change the command codes from standard ints to an enum
 */

package tablesproject.server;

import tablesproject.table.Table;
import tablesproject.table.TableEntry;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class TableServerThread extends Thread {
    private Socket s;
    private InputStream in;
    private OutputStream out;
    private TableServer tableServer;
    private boolean done = false;

    public TableServerThread(Socket s, InputStream in, OutputStream out, TableServer tableServer) {
        this.s = s;
        this.in = in;
        this.out = out;
        this.tableServer = tableServer;
        System.out.println("New tablesproject.client connected: " + s.getInetAddress());
    }

    @Override
    public void run() {
        while(!done) {
            try {
                int n = in.read();
                System.out.println(n);
                switch (n) {
                    case 0:
                        disconnect();
                        break;
                    case 1:
                        writeMap(tableServer.getMap());
                        break;
                    case 2:
                        tableServer.updateMap(readMap());
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
                System.out.println(tableServer.getMap().values());
            } catch (IOException ie) {
                System.out.println("Error with communicating with tablesproject.client");
            } catch (ClassNotFoundException ce) {
                System.out.println("Error with reading object");
            }
        }
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() throws IOException {
        out.write(0);
        done = true;
        System.out.println("TableClient from " + s.getInetAddress() + " disconnected");
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
        t = tableServer.mapGet(label);
        t.addEntry(e);
        tableServer.mapRemove(t);
        tableServer.mapPut(t);
    }

    public void removeEntry() throws IOException, ClassNotFoundException {
        out.write(5);

        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Table t = (Table) objectInputStream.readObject();
        String label = t.getLabel();
        TableEntry e = (TableEntry) objectInputStream.readObject();
        t = tableServer.mapGet(label);
        t.removeEntry(e);
        tableServer.mapRemove(t);
        tableServer.mapPut(t);
    }

    public void updateEntry() throws IOException, ClassNotFoundException {
        out.write(6);

        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Table t = (Table) objectInputStream.readObject();
        String label = t.getLabel();
        TableEntry e = (TableEntry) objectInputStream.readObject();
        t = tableServer.mapGet(label);
        t.update(e);
        tableServer.mapRemove(t);
        tableServer.mapPut(t);
    }

    public void addTable() throws IOException, ClassNotFoundException {
        out.write(3);

        ObjectInputStream objectInputStream = new ObjectInputStream((in));
//        String l = (String) objectInputStream.readObject();
        Table t = (Table) objectInputStream.readObject();
        tableServer.mapPut(t);
    }
}
