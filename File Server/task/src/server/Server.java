package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;



public class Server extends Main {
    private static final int PORT = 23456;
    private static final String ADDRESS = "127.0.0.1";
    private static final String MAPNAME = "storage.data";
    private static final String PATH = "E:\\Java IDEA projects\\File Server\\File Server\\task\\src\\server\\data";
    private StorageMap map;//hash map stores all unique identifiers for filenames
    Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean isExit = false;


    public Server() throws IOException, ClassNotFoundException {

        //when we start server we first check if hash map exists
        //if so - we deserialize its last state from the file
        if (Files.exists(Path.of(PATH + "\\" + MAPNAME))) {
            File file = new File(PATH + "\\" + MAPNAME);
            map = (StorageMap) SerializationUtils.deserialize(file);
        } else {
            map = new StorageMap();
        }
    }

    public void runServer() throws IOException {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            while (!isExit) {
                //start and initialize input/output streams
                start(server);

                //read the client prompt from input object stream
                String clientRequest = (String) input.readObject();


                switch (clientRequest.split(" ")[0]) {
                    case "PUT":
                        putFileOnServer(clientRequest, map, PATH, output, input);
                        break;
                    case "GET":
                        getFileFromServer(clientRequest, map, PATH, output);
                        break;
                    case "DELETE":
                        deleteFileFromServer(clientRequest, PATH, map, output);
                        break;
                    case "exit":
                        isExit = true;
                        break;
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //serializing hash map current state
        File file = new File(PATH + "\\" + MAPNAME);
        SerializationUtils.serialize(map, file);

    }

    public void start(ServerSocket server) throws IOException {
        socket = server.accept();
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());
    }



    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = new Server();
        System.out.println("Server started!");
        server.runServer();
    }
}
