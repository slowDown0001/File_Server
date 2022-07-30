package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ClientManager {

    private String clientCommand, fileContent;
    private static final Scanner scanner = new Scanner(System.in);
    private String filename;
    private String newFilename;

    public String prompt() throws IOException {
        scanner.useDelimiter("\r?\n");
        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String userResponse = scanner.next();
        switch (userResponse) {
            case "1":
                //clientCommand = "GET";
                return getFileRequest();
                //break;
            case "2":
                //clientCommand = "PUT";
                return createFileRequest();
                //break;
            case "3":
                //clientCommand = "DELETE";
                return deleteFileRequest();
                //break;
            case "exit":
                //clientCommand = "EXIT";
                return exitRequest();
                //break;
        }
        /*if (!clientCommand.equals("EXIT")) {
            System.out.print("Enter name of the file: ");
            filename = scanner.next();
            if (clientCommand.equals("PUT")) {
                //https://stackoverflow.com/questions/5689325/blank-input-from-scanner-java
                System.out.print("Enter name of the file to be saved on server: ");
                BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
                String line = buf.readLine();
                filename = line.equals("") ? filename : line;

                *//*if (filename.endsWith(".txt")) {
                    System.out.print("Enter file content: ");
                    fileContent = scanner.next();
                }*//*
            }
        }*/
        return null;
    }

    public String getFileRequest() throws IOException {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        int userResponse = scanner.nextInt();
        String prompt = userResponse == 1 ? "Enter name of the file: " : "Enter id: ";
        System.out.print(prompt);
        String idOrName = scanner.next();
        ClientCommand cc = (String c) -> c + " " + idOrName;
        return cc.sendCommand("GET");
    }

    public String createFileRequest() throws IOException {
        //https://stackoverflow.com/questions/5689325/blank-input-from-scanner-java
        //if there is a problem with scanner.nextLine() use buf.readLine()
        System.out.print("Enter name of the file: ");
        filename = scanner.next();
        System.out.print("Enter name of the file to be saved on server: ");
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        String line = buf.readLine();
        newFilename = line.equals("") ? filename : line;
        ClientCommand cc = (String c) -> c + " " + newFilename /*+ " " + fileContent*/;
        return cc.sendCommand("PUT");
    }
    public String getNewFilename() {return newFilename;}

    public String deleteFileRequest() {
        System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
        int userResponse = scanner.nextInt();
        String prompt = userResponse == 1 ? "Enter name of the file: " : "Enter id: ";
        System.out.print(prompt);
        String idOrName = scanner.next();
        ClientCommand cc = (String c) -> c + " " + idOrName;
        return cc.sendCommand("DELETE");
    }

    public String exitRequest() {
        return "exit";
    }

    public String getFilename() {
        return filename;
    }


    /*public static void main(String[] args) throws IOException {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        int userResponse = scanner.nextInt();
        String prompt = userResponse == 1 ? "Enter name of the file: " : "Enter id: ";
        System.out.print(prompt);
        String idOrName = scanner.next();
        ClientCommand cc = (String c) -> c + " " + idOrName;
        System.out.println(cc.sendCommand("GET"));

    }*/
}
