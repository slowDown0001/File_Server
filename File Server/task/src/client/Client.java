package client;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    private static final String PATH = "E:\\Java IDEA projects\\File Server\\File Server\\task\\src\\client\\data";
    private ObjectInputStream input;
    private ObjectOutputStream output;
    Socket socket;
    String filename;

    private ClientManager cm = new ClientManager();

    public void sendClientRequest() throws IOException, ClassNotFoundException {
        //prompt user for all necessary stuff
        String clientRequest = cm.prompt();

        //start client, connect to the server
        startClient();

        //send request and print "The request was sent". Here we ask server to PUT/GET/DELETE/EXIT
        sendInitialRequest(clientRequest);

        if (!clientRequest.split(" ")[0].equals("exit")) {
            String received = (String) input.readObject();

            switch (clientRequest.split(" ")[0]) {
                case "PUT":
                    if (checkIfSuccessful(received)) {
                        //filename = clientRequest.split(" ")[1];
                        filename = cm.getFilename();

                        SerializableFile sf = new SerializableFile(PATH, filename);
                        output.writeObject(sf);
                        output.flush();


                        System.out.println("Response says that file is saved! ID = " + received.substring(4));
                    } else {
                        System.out.println("The response says that creating the file was forbidden!");
                    }
                    break;

                case "GET":
                    if (checkIfSuccessful(received)) {
                        SerializableFile receivedFile = (SerializableFile) input.readObject();
                        System.out.print("The file was downloaded! Specify a name for it: ");
                        Scanner scanner = new Scanner(System.in);
                        String filename = scanner.next();
                        File file = new File(PATH + "\\" + filename);
                        receivedFile.copyToTheFile(file);
                        System.out.println("File saved on the hard drive!");
                    } else {
                        System.out.println("The response says that this file is not found!");
                    }
                    break;
                case "DELETE":
                    if (checkIfSuccessful(received)) {
                        System.out.println("The response says that this file was deleted successfully!");
                    } else {
                        System.out.println("The response says that this file is not found!");
                    }


            }
        }

        output.flush();
        output.close();
        input.close();
        socket.close();

    }

    public boolean checkIfSuccessful(String serverResponse) {
        return serverResponse.startsWith("200");
    }

    public void sendInitialRequest(String htmlRequest) {
        try {
            output.writeObject(htmlRequest);
            output.flush();
            System.out.println("The request was sent.");
        } catch (IOException e) {
            System.out.println("Something wrong with sending initial request to the server");
        }
    }

    public void startClient() throws IOException {
        socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();

        input = new ObjectInputStream(socket.getInputStream());
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client();
        client.sendClientRequest();
    }
}
