package table;

import java.io.Serializable;
import java.util.HashMap;

public class Table implements Serializable {
    private String label;
    //    private ArrayList<TableEntry> list = new ArrayList<>();
    private HashMap<String, TableEntry> map = new HashMap<>();

    public Table(String l) {
        this.label = l;
    }

    public void setMap(HashMap<String, TableEntry> map) {
        this.map = map;
    }

    public String getLabel() {
        return label;
    }

    public void addEntry(TableEntry e) {
//        list.add(e);
        map.put(e.getLabel(), e);
        e.setParentTable(this);
    }

    public void removeEntry(TableEntry e) {
        map.remove(e.getLabel());
    }

    //returns a TableEntry or null if it doesn't exist
    public TableEntry getEntry(String s) {
        return map.get(s);
    }

    //returns 0 if successful, -1 otherwise
    public int update(TableEntry e) {
        if(map.get(e.getLabel()) != null) {
            map.remove(e.getLabel());
            map.put(e.getLabel(), e);
            return 0;
        }
        return -1;
    }

}