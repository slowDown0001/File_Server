package server;

import java.io.*;

public class SerializationUtils {
    /**
     * Serialize the given object to the file
     */
    public static void serialize(Object obj, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }
    /**
     * Deserialize to an object from the file
     */
    public static Object deserialize(File file) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}

