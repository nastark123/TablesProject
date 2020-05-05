import java.io.*;
import java.util.HashMap;
import table.Table;
import table.TableEntry;


public class ClientMain {
    public static void main(String[] args) throws IOException,ClassNotFoundException {
        Client client = new Client("localhost", 967);

        HashMap<String, Table> map = client.getMap();

        Table doubleTable = new Table("double");
        client.addTable(doubleTable);

        TableEntry x = new TableEntry("x");
        x.setDoubleValue(15.0);
        TableEntry y = new TableEntry("y");
        y.setDoubleValue(30.0);

        client.addEntry(doubleTable, x);
        client.addEntry(doubleTable, y);

        client.readMap();
        map = client.getMap();

        System.out.println(map.get("double").getEntry("x").getDoubleValue());
        System.out.println(map.get("double").getEntry("y").getDoubleValue());
    }

}
