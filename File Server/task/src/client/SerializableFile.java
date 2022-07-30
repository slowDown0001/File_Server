package client;


import java.io.*;

public class SerializableFile implements Serializable {
    //private static final String PATH = "E:\\Java IDEA projects\\File Server\\File Server\\task\\src\\client\\data";
    private File file;


    public SerializableFile(String path, String filename) {
        file = new File(path + "\\" + filename);
    }

    public File getFile() {
        return file;
    }

    public void copyToTheFile(File b) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(b);
        try {

            int n;

            // read() function to read the
            // byte of data
            while ((n = in.read()) != -1) {
                // write() function to write
                // the byte of data
                out.write(n);
            }
        } finally {
            if (in != null) {

                // close() function to close the
                // stream
                in.close();
            }
            // close() function to close
            // the stream
            if (out != null) {
                out.close();
            }
        }

    }

}

