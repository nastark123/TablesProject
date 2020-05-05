import java.io.*;
import java.net.*;
import java.util.HashMap;
import table.Table;
import table.TableEntry;

public class Server {
    private Socket socket;
    private ServerSocket sSocket;
    private InputStream in;
    private OutputStream out;
    private volatile static HashMap<String, Table> map;

    public Server(int port) throws IOException {
        sSocket = new ServerSocket(port);
        map = new HashMap<>();
    }

    public void waitForConn() throws IOException {
        while(true) {
            socket = sSocket.accept();
            in = socket.getInputStream();
            out = socket.getOutputStream();
            ServerThread thread = new ServerThread(socket, in, out, this);
            thread.start();
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
