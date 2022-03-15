package career.projects;

import java.util.ArrayList;
import java.util.List;

public class Column {
    enum Type{
        INTEGER, STRING
    }

    String culName;
    Type type;
    List<String> stringData;
    List<Integer> intData;

    public Column(String name, Type t){
        stringData = new ArrayList<>();
        intData = new ArrayList<>();
        this.culName = name;
        this.type = t;
    }
}
