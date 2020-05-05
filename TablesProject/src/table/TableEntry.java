package table;

import java.io.Serializable;

public class TableEntry implements Serializable {
    private String label;
    private Value value;
    private Table parentTable;

    protected TableEntry(String l, Object o, Types t) {
        this.label = l;
        this.value = new Value(o, t);
    }

    protected TableEntry(String l, Value v) {
        this.label = l;
        this.value = v;
    }

    public TableEntry(String l) {
        this.label = l;
        this.value = null;
    }

    public String getLabel() {
        return label;
    }

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
    }

    public Table getParentTable() {
        return this.parentTable;
    }

    public void setByteValue(Byte b) {
        value = new Value(b, Types.BYTE);
    }

    public void setIntValue(Integer i) {
        value = new Value(i, Types.INT);
    }

    public void setShortValue(Short s) {
        value = new Value(s, Types.SHORT);
    }

    public void setLongValue(Long l) {
        value = new Value(l, Types.LONG);
    }

    public void setDoubleValue(Double d) {
        value = new Value(d, Types.DOUBLE);
    }

    public void setFloatValue(Float f) {
        value = new Value(f, Types.FLOAT);
    }

    public void setCharValue(Character c) {
        value = new Value(c, Types.CHAR);
    }

    public void setStringValue(String s) {
        value = new Value(s, Types.STRING);
    }

    public Value getRawValue() {
        return value;
    }

    //returns the double value if the value is a double, or defaults to 0.0 otherwise
    public Double getDoubleValue() {
        return (value.getType() == Types.DOUBLE) ? (Double) value.getValue() : 0.0;
    }

    //returns the int value if the value is a int, or defaults to 0 otherwise
    public Integer getIntValue() {
        return (value.getType() == Types.INT) ? (Integer) value.getValue() : 0;
    }

    //returns the short value if the value is a short, or defaults to 0 otherwise
    public Short getShortValue() {
        return (value.getType() == Types.SHORT) ? (Short) value.getValue() : 0;
    }

    //returns the long value if the value is a long, or defaults to 0 otherwise
    public Long getLongValue() {
        return (value.getType() == Types.LONG) ? (Long) value.getValue() : 0;
    }

    //returns the char value if the value is a char, or defaults to a space otherwise
    public Character getCharValue() {
        return (value.getType() == Types.CHAR) ? (Character) value.getValue() : ' ';
    }

    //returns the String value if the value is a String, or defaults to a space otherwise
    public String getStringValue() {
        return (value.getType() == Types.STRING) ? (String) value.getValue() : " ";
    }

    //returns the byte value if the value is a byte, or defaults to 0 otherwise
    public Byte getByteValue() {
        return (value.getType() == Types.BYTE) ? (Byte) value.getValue() : 0;
    }

    //returns the float value if the value is a float, or defaults to 0.0f otherwise
    public Float getFloatValue() {
        return (value.getType() == Types.FLOAT) ? (Float) value.getValue() : 0.0f;
    }
}
