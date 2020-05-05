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
//    public void main(String[] args) throws IOException,ClassNotFoundException {
//        socket = new Socket("localhost", 967);
//        in = socket.getInputStream();
//        out = socket.getOutputStream();
//
//        HashMap<String, Table> map = readMap();
//
//        Table doubleTable = new Table("double");
//        addTable(doubleTable);
//
//        TableEntry x = new TableEntry("x");
//        x.setDoubleValue(15.0);
//        TableEntry y = new TableEntry("y");
//        y.setDoubleValue(30.0);
//
//        addEntry(doubleTable, x);
//        addEntry(doubleTable, y);
//
//        map = readMap();
//
//        System.out.println(map.get("double").getEntry("x").getDoubleValue());
//        System.out.println(map.get("double").getEntry("y").getDoubleValue());

//        Table tab1 = new Table("test");
//        TableEntry entry = new TableEntry("testentry");
//        entry.setIntValue(10);
//        tab1.addEntry(entry);
//        map.put(tab1.getLabel(), tab1);
//
//        writeMap(map);
//
//        Table tab2 = new Table("test2");
//        TableEntry entry2 = new TableEntry("testentry");
//        entry2.setIntValue(20);
//        tab2.addEntry(entry2);
//
//        addTable(tab2);
//
//        TableEntry entry3 = new TableEntry("testint");
//        entry3.setIntValue(50);
//
//        addEntry(tab2, entry3);
//
//        map = readMap();
//        System.out.println("First map entry value: " + map.get("test").getEntry("testentry").getIntValue());
//        System.out.println("Second map entry value: " + map.get("test2").getEntry("testentry").getIntValue());
//        System.out.println("Third map entry value: " + map.get("test2").getEntry("testint").getIntValue());
//
//        entry2.setIntValue(100);
//
//        updateEntry(tab2, entry2);
//
//        map = readMap();
//        System.out.println("First map entry value: " + map.get("test").getEntry("testentry").getIntValue());
//        System.out.println("Second map entry value: " + map.get("test2").getEntry("testentry").getIntValue());
//        System.out.println("Third map entry value: " + map.get("test2").getEntry("testint").getIntValue());
//    }
    
    public Client(String ip, int port) throws IOException, ClassNotFoundException {
        socket = new Socket("localhost", 967);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        map = new HashMap<>();
        readMap();
    }

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

    public synchronized void addTable(Table t) throws IOException {
        out.write(3);

        int n = in.read();
        if(n == 3) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
//            objectOutputStream.writeObject(l);
            objectOutputStream.writeObject(t);
        }
    }

    public synchronized void addEntry(Table t, TableEntry e) throws IOException {
        out.write(4);

        int n = in.read();
        if(n == 4) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(t);
            objectOutputStream.writeObject(e);
        }
    }

    public synchronized void updateEntry(Table t, TableEntry e) throws IOException {
        out.write(6);

        int n = in.read();
        if(n == 6) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(t);
            objectOutputStream.writeObject(e);
        }
    }

    public synchronized void removeEntry(Table t, TableEntry e) throws IOException {
        out.write(5);

        int n = in.read();
        if(n == 5) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(t);
            objectOutputStream.writeObject(e);
        }
    }

    public synchronized void writeMap(HashMap<String, Table> map) throws IOException {
        out.write(2);

        int n = in.read();
        if(n == 2) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(map);
        }
    }

    public synchronized HashMap<String, Table> getMap() {
        return map;
    }

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
