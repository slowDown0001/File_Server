package server;

import java.io.Serializable;
import java.util.HashMap;

public class StorageMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<Integer,String> identifiers;

    public StorageMap () {
        identifiers = new HashMap<>();
    }

    public HashMap<Integer, String> getMap() { return identifiers;}
    public int generateId (String filename) {
        return filename.hashCode()/100000;
    }

    public String addFileName (String filename) {
        return identifiers.putIfAbsent(generateId(filename), filename);
    }

    public String findNameById (int id) {
        return identifiers.getOrDefault(id, "404");
    }

    public void removeByName (String filename) {
        identifiers.values().remove(filename);
    }

    public void removeById (int id) {
        identifiers.remove(id);
    }

    public String toString() {
        return identifiers.toString();
    }


}
