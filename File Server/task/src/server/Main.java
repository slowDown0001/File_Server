package server;

import client.SerializableFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//Some useful methods
public abstract class Main {

    private static final String SUCCESS = "200";
    private static final String FORBIDDEN = "403";
    private static final String NOT_FOUND_ERROR = "404";



    //https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
    public String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }


    public static void writeToFiles(File file, String content) throws IOException {
        CharArrayWriter contactWriter = new CharArrayWriter();
        FileWriter fw = new FileWriter(file, true);

        contactWriter.write(content);
        contactWriter.writeTo(fw);

        fw.close();
        contactWriter.close();
    }

    public void putFileOnServer (String clientRequest, StorageMap map, String PATH, ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        String[] request = clientRequest.split(" ");
        String fileName = /*"onServer_" + */request[1];
        if(!Files.exists(Path.of(PATH + "\\" + fileName))) {
            map.addFileName(fileName);

            output.writeObject(SUCCESS + " " + map.generateId(fileName));
            output.flush();

            SerializableFile newFileOnServer = (SerializableFile) input.readObject();

            File file = new File(PATH + "\\" + fileName);
            newFileOnServer.copyToTheFile(file);
        } else {
            output.writeObject(FORBIDDEN);
            output.flush();
        }
    }


    public void getFileFromServer(String clientRequest, StorageMap map, String PATH, ObjectOutputStream output) throws IOException {
        String[] request = clientRequest.split(" ");
        if (request[1].contains(".")) {
            String fileName = /*"onServer_" + */request[1];
            if (Files.exists(Path.of(PATH + "\\" + fileName))) {

                try {

                    sendFile(fileName, PATH, output);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {

                output.writeObject(NOT_FOUND_ERROR);
                output.flush();
            }

        } else {

            String fileId = request[1];

            String fileName = map.findNameById(Integer.parseInt(fileId));

            if (fileName.equals(NOT_FOUND_ERROR)) {
                output.writeObject(fileName);
                output.flush();
            } else {
                try {

                    sendFile(fileName, PATH, output);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public void deleteFileFromServer(String clientRequest, String PATH, StorageMap map, ObjectOutputStream output) throws IOException {
        String[] request = clientRequest.split(" ");
        if (request[1].contains(".")) {
            String fileName = /*"onServer_" + */request[1];
            if (Files.exists(Path.of(PATH + "\\" + fileName))) {

                deleteFile(fileName, PATH, map, output);

            } else {
                output.writeObject(NOT_FOUND_ERROR);
                output.flush();
            }

        } else {

            String fileId = request[1];
            String fileName = map.findNameById(Integer.parseInt(fileId));

            if (fileName.equals(NOT_FOUND_ERROR)) {
                output.writeObject(fileName);
                output.flush();
            } else {
                deleteFile(fileName, PATH, map, output);
            }
        }
    }

    public void sendFile(String fileName, String PATH, ObjectOutputStream output) throws IOException {
        if (!fileName.contains(".txt")) {
            output.writeObject(SUCCESS);
        } else {
            output.writeObject(SUCCESS + " " + readFile(PATH + "\\" + fileName));
        }
        output.flush();
        //serialize file to be sent
        SerializableFile fileToClient = new SerializableFile(PATH, fileName);
        //sent it to the client
        output.writeObject(fileToClient);
        output.flush();
    }

    public void deleteFile(String fileName, String PATH, StorageMap map, ObjectOutputStream output) throws IOException {
        map.removeByName(fileName);
        File fileRoDelete = new File(PATH + "\\" + fileName);
        fileRoDelete.delete();
        output.writeObject(SUCCESS);
        output.flush();
    }

}



