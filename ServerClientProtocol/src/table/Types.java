package table;

public enum Types {
    BOOLEAN(0),
    BYTE(1),
    CHAR(2),
    SHORT(3),
    INT(4),
    LONG(5),
    FLOAT(6),
    DOUBLE(7),
    STRING(8),
    OBJECT(9);

    private int i;

    Types(int i) {
        this.i = i;
    }
}
