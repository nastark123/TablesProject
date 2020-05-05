package table;

import java.io.Serializable;

public class Value implements Serializable {
    private Object value;
    private Types type;

    public Value(Object v, Types t) {
        this.value = v;
        this.type = t;
    }

    public Object getValue() {
        return value;
    }

    public Types getType() {
        return type;
    }
}
