package tablesproject.table;

import java.io.Serializable;
import java.util.HashMap;

public class Table implements Serializable {
    private String label;
    private HashMap<String, TableEntry> map = new HashMap<>();

    public Table(String l) {
        this.label = l;
    }

    //sets the HashMap that represents this tablesproject.table to the one specified, would not recommend using
    public void setMap(HashMap<String, TableEntry> map) {
        this.map = map;
    }

    //returns the label of this tablesproject.table (pretty much its name)
    public String getLabel() {
        return label;
    }

    //adds the specified entry to this tablesproject.table
    public void addEntry(TableEntry e) {
        map.put(e.getLabel(), e);
        e.setParentTable(this);
    }

    //similar to addEntry, but adds a subtable instead
    public void addSubtable(Table t) {
        map.put(t.getLabel(), new TableEntry(t.getLabel(), t, Types.TABLE));
    }

    //removes the specified entry from the tablesproject.table
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